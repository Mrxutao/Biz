package com.xutao.itrip.service.impl;

import java.math.BigDecimal;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xutao.itrip.mapper.ItripCommentMapper;
import com.xutao.itrip.mapper.ItripHotelOrderMapper;
import com.xutao.itrip.beans.pojo.ItripComment;
import com.xutao.itrip.beans.pojo.ItripImage;
import com.xutao.itrip.beans.vo.comment.ItripListCommentVO;
import com.xutao.itrip.beans.vo.comment.ItripScoreCommentVO;
import com.xutao.itrip.service.ItripCommentService;
import com.xutao.itrip.service.ItripImageService;
import com.xutao.itrip.utils.BigDecimalUtil;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 评论表
 * @author 许涛
 *
 */
@Service
public class ItripCommentServiceImpl implements ItripCommentService {

	private Logger logger=Logger.getLogger(ItripCommentServiceImpl.class);
	@Resource
	private ItripCommentMapper itripCommentMapper;
	@Resource
	private ItripImageService ItripImageService;
	@Resource 
	private ItripHotelOrderMapper itripHotelOrderMapper;
	
	/**
	 * 需求:根据id查询
	 */
	public ItripComment getItripCommentById(Long id) throws Exception {
		return itripCommentMapper.getItripCommentById(id);
	}

	/**
	 * 需求:查询评论列表
	 */
	public List<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param) throws Exception {
		return itripCommentMapper.getItripCommentListByMap(param);
	}

	/**
	 * 需求:查询评论总记录数
	 */
	public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
		return itripCommentMapper.getItripCommentCountByMap(param);
	}

	/**
	 * 需求:新增评论
	 */
	public boolean itriptxAddItripComment(ItripComment itripComment, List<ItripImage> itripImages) throws Exception {
		if(itripComment!=null) {
			//计算综合评分，综合评分=(设施+卫生+位置+服务)/4
			float score=0;
			int sum=itripComment.getFacilitiesScore()+itripComment.getHygieneScore()+itripComment.getPositionScore()+itripComment.getServiceScore();
			score=BigDecimalUtil.OperationASMD(sum, 4, BigDecimalUtil.BigDecimalOprations.divide, 1, BigDecimal.ROUND_DOWN).floatValue();
			//对结果四首五入
			itripComment.setScore(Math.round(score));
			Long commentId=1L;
			if(itripCommentMapper.insertItripComment(itripComment)>0) {
				commentId=itripComment.getId();
				logger.debug("新增评论id:====="+commentId);
				if(null!=itripImages && itripImages.size()>0 && commentId>0) {
					for(ItripImage i:itripImages) {
						i.setTargetId(commentId);
					}
				}
				//更新评论为已评论
				itripHotelOrderMapper.updateHotelOrderStatus(itripComment.getOrderId(), itripComment.getCreatedBy());
			}
		}
		return false;
	}
	
	/**
	 * 需求:更新评论
	 */
	public Integer itriptxModifyItripComment(ItripComment itripComment) throws Exception {
		itripComment.setModifyDate(new Date());
		return itripCommentMapper.updateItripComment(itripComment);
	}

	/**
	 * 需求:删除评论
	 */
	public Integer itriptxDeleteItripCommentById(Long id) throws Exception {
		return itripCommentMapper.deleteItripCommentById(id);
	}

	/**
	 * 需求:查询评论列表，以分页显示
	 */
	public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo,
			Integer pageSize) throws Exception {
		Integer total=itripCommentMapper.getItripCommentCountByMap(param);
		pageNo=EmptyUtils.isEmpty(pageNo) ?Constants.DEFAULT_PAGE_NO :pageNo;
		pageSize=EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE :pageSize;
		Page page=new Page(pageNo,pageSize,total);
		param.put("beginPos", page.getBeginPos());
		param.put("pageSize", page.getPageSize());
		List<ItripListCommentVO>list=itripCommentMapper.getItripCommentListByMap(param);
		page.setRows(list);
		return page;
	}

	/**
	 * 需求:求平均值
	 */
	public ItripScoreCommentVO getAvgAndTotalScore(Long hotelId) throws Exception {
		return itripCommentMapper.getCommentAvgScore(hotelId);
	}

}
