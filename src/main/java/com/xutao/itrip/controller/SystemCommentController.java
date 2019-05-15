package com.xutao.itrip.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.xutao.itrip.beans.dto.Dto;
import com.xutao.itrip.beans.pojo.ItripComment;
import com.xutao.itrip.beans.pojo.ItripImage;
import com.xutao.itrip.beans.pojo.ItripUser;
import com.xutao.itrip.beans.vo.comment.ItripAddCommentVO;
import com.xutao.itrip.beans.vo.comment.ItripScoreCommentVO;
import com.xutao.itrip.service.ItripCommentService;
import com.xutao.itrip.service.ItripHotelService;
import com.xutao.itrip.service.ItripImageService;
import com.xutao.itrip.service.ItripLabelDicService;
import com.xutao.itrip.utils.DtoUtil;
import com.xutao.itrip.utils.SystemConfig;
import com.xutao.itrip.utils.ValidationToken;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 评论Controller
 * 
 * 包括API接口: 1、根据type 和 target id查询评论照片 2、据酒店id查询平均分(总体评分、位置评分、设施评分、服务评分、卫生评分)
 * 3、根据酒店id查询评论数量 4、根据评论类型查询评分 分页 5、上传评论图片 6、删除评论图片 7、新增评论信息 8、查看个人评论信息
 * 9、查询出游类型列表 10、新增评论信息页面获取酒店相关信息(酒店名称、酒店图片、酒店星级)
 * 
 * 注:错误码(100001--100100)
 * 
 * @author 许涛
 *
 */
@Controller
@RequestMapping(value = "/api/comment")
public class SystemCommentController {

	private Logger logger = Logger.getLogger(SystemCommentController.class);
	@Resource
	private SystemConfig systemConfig;
	@Resource
	private ItripCommentService itripCommentService;
	@Resource
	private ValidationToken validationToken;
	@Resource
	private ItripImageService itripImageService;
	@Resource
	private ItripLabelDicService itripLabelDicService;
	@Resource
	private ItripHotelService itripHotelService;

	@RequestMapping(value = "/getHotelScore/{hotelId}", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "根据酒店id查询酒店评分", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "总体评分、位置评分、设施评分、服务评分、卫生评分"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>" + "<p>100001 : 获取评分失败 </p>"
			+ "<p>100002 : hotelId不能为空</p>")
	@ResponseBody
	public Dto<Object> getHotelScore(
			@ApiParam(required = true, name = "hotelId", value = "酒店Id") @PathVariable String hotelId) {
		Dto<Object> dto = new Dto<Object>();
		logger.debug("hotelID" + hotelId);
		if (hotelId != null && !"".equals(hotelId)) {
			ItripScoreCommentVO itripScoreCommentVO = new ItripScoreCommentVO();
			try {
				itripScoreCommentVO = itripCommentService.getAvgAndTotalScore(Long.parseLong(hotelId));
				dto = DtoUtil.returnSuccess("获取评分成功!", itripScoreCommentVO);
			} catch (Exception e) {
				e.printStackTrace();
				dto = DtoUtil.returnFail("获取评分失败!", "100001");
			}
		} else {
			dto = DtoUtil.returnFail("hotelId不能为空", "100002");
		}
		return dto;
	}

	@ApiOperation(value = "新增评论接口", httpMethod = "POST", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "新增评论信息"
			+ "<p style=‘color:red’>注意：若有评论图片，需要传图片路径</p>"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>" + "<p>100003 : 新增评论失败 </p>"
			+ "<p>100004 : 不能提交空，请填写评论信息</p>" + "<p>100005 : 新增评论，订单ID不能为空</p>" + "<p>100000 : token失效，请重登录 </p>")
	@RequestMapping(value = "/addComment", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Dto<Object> addComment(@RequestBody ItripAddCommentVO itripAddCommentVO, HttpServletRequest request) {
		Dto<Object> dto = new Dto<Object>();
		String tokenString = request.getHeader("token");
		ItripUser user = validationToken.getCurrentUser(tokenString);
		if (user != null && null != itripAddCommentVO) {
			// 新增评论，不能为空
			if (itripAddCommentVO.getOrderId() == null || itripAddCommentVO.getOrderId() == 0) {
				return DtoUtil.returnFail("新增评论ID,订单ID不能为空", "100005");
			}
			List<ItripImage> itripImages = null;
			ItripComment itripComment = new ItripComment();
			itripComment.setContent(itripAddCommentVO.getContent());
			itripComment.setHotelId(itripAddCommentVO.getHotelId());
			itripComment.setIsHavingImg(itripAddCommentVO.getIsHavingImg());
			itripComment.setPositionScore(itripAddCommentVO.getPositionScore());
			itripComment.setFacilitiesScore(itripAddCommentVO.getFacilitiesScore());
			itripComment.setHygieneScore(itripAddCommentVO.getHygieneScore());
			itripComment.setOrderId(itripAddCommentVO.getOrderId());
			itripComment.setServiceScore(itripAddCommentVO.getServiceScore());
			itripComment.setProductId(itripAddCommentVO.getProductId());
			itripComment.setProductType(itripAddCommentVO.getProductType());
			itripComment.setIsOk(itripAddCommentVO.getIsOk());
			itripComment.setTripMode(itripAddCommentVO.getTripMode());
			itripComment.setCreatedBy(user.getId());
			itripComment.setCreationDate(new Date(System.currentTimeMillis()));
			itripComment.setUserId(user.getId());
			try {
				if (itripAddCommentVO.getIsHavingImg() == 1) {
					itripImages = new ArrayList<ItripImage>();
					int i = 1;
					for (ItripImage itripImage : itripAddCommentVO.getItripImages()) {
						itripImage.setCreatedBy(user.getId());
						itripImage.setPosition(i);
						itripImage.setCreationDate(itripComment.getCreationDate());
						itripImage.setType("2");
						itripImages.add(itripImage);
						i++;
					}
				}
				itripCommentService.itriptxAddItripComment(itripComment,
						(null == itripImages ? new ArrayList<ItripImage>() : itripImages));
				dto = DtoUtil.returnSuccess("添加评论成功");
			} catch (Exception e) {
				e.printStackTrace();
				dto = DtoUtil.returnFail("添加评论失败", "100003");
			}
		} else if (itripAddCommentVO == null && user == null) {
			dto = DtoUtil.returnFail("不能提交为空,请填写评论信息", "100004");
		} else {
			dto = DtoUtil.returnFail("token失效，请重新登录。", "100000");
		}
		return dto;
	}

	@ApiOperation(value = "文件上传", httpMethod = "POST", protocols = "HTTP", produces = "application/json")
	@RequestMapping(value = "uploadPic")
	public Dto<Object> uploadPic(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		Dto<Object> dto = new Dto<Object>();
		List<String> dataList = new ArrayList<String>();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			int fileCount = 0;
			try {
				fileCount = multiRequest.getFileMap().size();
			} catch (Exception e) {
				// TODO: handle exception
				fileCount = 0;
				return DtoUtil.returnFail("文件大小超限", "100009");
			}
			logger.debug("user upload files count: " + fileCount);

			if (fileCount > 0 && fileCount <= 4) {
				String tokenString = multiRequest.getHeader("token");
				logger.debug("token name is from header : " + tokenString);
				ItripUser itripUser = validationToken.getCurrentUser(tokenString);
				if (null != itripUser) {
					logger.debug("user not null and id is : " + itripUser.getId());
					// 取得request中的所有文件名
					Iterator<String> iter = multiRequest.getFileNames();
					while (iter.hasNext()) {
						try {
							// 取得上传文件
							MultipartFile file = multiRequest.getFile(iter.next());
							if (file != null) {
								// 取得当前上传文件的文件名称
								String myFileName = file.getOriginalFilename();
								// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
								if (myFileName.trim() != "" && (myFileName.toLowerCase().contains(".jpg")
										|| myFileName.toLowerCase().contains(".jpeg")
										|| myFileName.toLowerCase().contains(".png"))) {
									// 重命名上传后的文件名
									// 命名规则：用户id+当前时间+随机数
									String suffixString = myFileName.substring(file.getOriginalFilename().indexOf("."));
									String fileName = itripUser.getId() + "-" + System.currentTimeMillis() + "-"
											+ ((int) (Math.random() * 10000000)) + suffixString;
									// 定义上传路径
									String path = systemConfig.getFileUploadPathString() + File.separator + fileName;
									logger.debug("uploadFile path : " + path);
									File localFile = new File(path);
									file.transferTo(localFile);
									dataList.add(systemConfig.getVisitImgUrlString() + fileName);
								}
							}
						} catch (Exception e) {
							continue;
						}
					}
					dto = DtoUtil.returnSuccess("文件上传成功", dataList);
				} else {
					dto = DtoUtil.returnFail("文件上传失败", "100006");
				}
			} else {
				dto = DtoUtil.returnFail("上传的文件数不正确，必须是大于1小于等于4", "100007");
			}
		} else {
			dto = DtoUtil.returnFail("请求的内容不是上传文件的类型", "100008");
		}
		return dto;
	}

	@ApiOperation(value = "图片删除接口", httpMethod = "POST", produces = "application/json", response = Dto.class, notes = "删除传递图片名称"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100010 : 文件不存在，删除失败 </p>" + "<p>100000 : token失效，请重登录 </p>")
	@RequestMapping(value = "delPic", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Dto<Object> delPic(@RequestParam String imgName, HttpServletRequest request) {
		String tokenString = request.getHeader("token");
		ItripUser cruuentUser = validationToken.getCurrentUser(tokenString);
		logger.debug("luji" + imgName);
		Dto<Object> dto = new Dto<Object>();
		if (cruuentUser != null) {
			String path = systemConfig.getFileUploadPathString() + File.pathSeparator + imgName;
			logger.debug("luji" + path);
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				dto = DtoUtil.returnSuccess("删除成功!");
			} else {
				dto = DtoUtil.returnFail("文件不存在，删除失败", "100010");
			}
		} else {
			dto = DtoUtil.returnFail("token失效，请重新登录。", "100000");
		}
		return dto;
	}

}
