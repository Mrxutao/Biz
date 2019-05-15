package com.xutao.itrip.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xutao.itrip.beans.dto.Dto;
import com.xutao.itrip.beans.pojo.ItripOrderLinkUser;
import com.xutao.itrip.beans.pojo.ItripUser;
import com.xutao.itrip.beans.pojo.ItripUserLinkUser;
import com.xutao.itrip.beans.vo.userinfo.ItripAddUserLinkUserVO;
import com.xutao.itrip.beans.vo.userinfo.ItripModifyUserLinkUserVO;
import com.xutao.itrip.beans.vo.userinfo.ItripSearchUserLinkUserVO;
import com.xutao.itrip.service.ItripOrderLinkUserService;
import com.xutao.itrip.service.ItripUserLinkUserService;
import com.xutao.itrip.utils.DtoUtil;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.ValidationToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/userinfo")
public class UserInfoController {

	@Autowired
	private ItripUserLinkUserService itripSearchService;

	private Logger logger = Logger.getLogger(UserInfoController.class);

	@Resource
	private ValidationToken validationToken;
	@Resource
	private ItripOrderLinkUserService itripOrderLinkUserService;

	/**
	 * 根据id姓名查询常用联系人
	 * 
	 * @param itripSearchUserLinkUserVO
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "查询常用联系人接口", httpMethod = "POST", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "查询常用联系人信息(可根据联系人姓名进行模糊查询)"
			+ "<p>若不根据联系人姓名进行查询，不输入参数即可 | 若根据联系人姓名进行查询，须进行相应的入参，比如：{\"linkUserName\":\"张三\"}</p>"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100401 : 获取常用联系人信息失败 </p>" + "<p>100000 : token失效，请重登录</p>")
	@RequestMapping(value = "/queryuserlinkuser", method = RequestMethod.POST)
	@ResponseBody
	public Dto<ItripUserLinkUser> queryUserLinkUser(@RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO,
			HttpServletRequest request) {
		// 从头部获取token，根据token获取的登录信息
		String tokenString = request.getHeader("token");
		logger.debug("token name is from header : " + tokenString);
		ItripUser currentUser = validationToken.getCurrentUser(tokenString);

		List<ItripUserLinkUser> userLinkUserList = new ArrayList<ItripUserLinkUser>();
		String linkUserName = (null == itripSearchUserLinkUserVO) ? null : itripSearchUserLinkUserVO.getLinkUserName();
		Dto dto = null;
		if (null != currentUser) {
			// 查询条件
			Map param = new HashMap();
			param.put("userId", currentUser.getId());
			param.put("linkUserName", linkUserName);
			try {
				userLinkUserList = itripSearchService.getItripUserLinkUserListByMap(param);
				return DtoUtil.returnSuccess("获取常用联系人", userLinkUserList);
			} catch (Exception e) {
				e.printStackTrace();
				return DtoUtil.returnFail("获取常用联系人信息失败", "100401");
			}
		} else {
			return DtoUtil.returnFail("token失效，请重新登录", "100000");
		}
	}

	/**
	 * 新增常用联系人
	 * 
	 * @param itripAddUserLinkUserVO
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "新增常用联系人接口", httpMethod = "POST", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "新增常用联系人接口")
	@RequestMapping(value = "/adduserlinkuser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Dto<Object> addUserLinkUser(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO,
			HttpServletRequest request) {
		// 从头部获取token，根据token获取登录信息
		String tokenString = request.getHeader("token");
		ItripUser user = validationToken.getCurrentUser(tokenString);

		if (user != null && null != itripAddUserLinkUserVO) {
			ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
			itripUserLinkUser.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
			itripUserLinkUser.setLinkIdCardType(itripAddUserLinkUserVO.getLinkIdCardType());
			itripUserLinkUser.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
			itripUserLinkUser.setUserId(user.getId());
			itripUserLinkUser.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
			itripUserLinkUser.setCreatedBy(user.getId());
			itripUserLinkUser.setCreationDate(new Date(System.currentTimeMillis()));
			try {
				itripSearchService.addUserLinkUser(itripUserLinkUser);
			} catch (Exception e) {
				e.printStackTrace();
				return DtoUtil.returnFail("新增常用联系人失败", "100411");
			}
			return DtoUtil.returnSuccess("新增常用联系人成功");
		} else if (null != user && null != itripAddUserLinkUserVO) {
			return DtoUtil.returnFail("不能提交空，请填写常用联系人信息", "100412");
		} else {
			return DtoUtil.returnFail("token失效，请重新登录", "100000");
		}
	}

	/**
	 * 修改常用联系人
	 * 
	 * @param itripModifyUserLinkUserVO
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "更新常用联系人", httpMethod = "POST", protocols = "HTTP", produces = "application/json")
	@RequestMapping(value = "/modifyuserlinkuser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Dto<Object> updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO,
			HttpServletRequest request) {
		// 从头部获取token，从token中获取登录信息
		String StringToken = request.getHeader("token");
		ItripUser cruuentUser = validationToken.getCurrentUser(StringToken);
		if (cruuentUser != null && itripModifyUserLinkUserVO != null) {
			ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
			itripUserLinkUser.setId(itripModifyUserLinkUserVO.getId());
			itripUserLinkUser.setLinkUserName(itripUserLinkUser.getLinkUserName());
			itripUserLinkUser.setLinkIdCardType(itripModifyUserLinkUserVO.getLinkIdCardType());
			itripUserLinkUser.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
			itripUserLinkUser.setUserId(cruuentUser.getId());
			itripUserLinkUser.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
			itripUserLinkUser.setModifiedBy(cruuentUser.getId());
			itripUserLinkUser.setModifyDate(new Date(System.currentTimeMillis()));
			try {
				itripSearchService.updateUserLinkUser(itripUserLinkUser);

			} catch (Exception e) {
				e.printStackTrace();
				return DtoUtil.returnFail("修改常用联系人失败", "100421");
			}
			return DtoUtil.returnSuccess("修改常用联系人成功");
		} else if (null != cruuentUser && itripModifyUserLinkUserVO == null) {
			return DtoUtil.returnFail("不能提交空，请填写常用联系人信息", "100422");
		} else {
			return DtoUtil.returnFail("token失效，请重新登录", "100000");
		}
	}

	public Dto<Object> delUserLinkUser(Long[] ids, HttpServletRequest request) {
		String tokenString = request.getHeader("token");
		logger.debug("Token" + tokenString);
		ItripUser cruuentUser = validationToken.getCurrentUser(tokenString);
		List<Long> idsList = new ArrayList<Long>();
		if (null != cruuentUser && EmptyUtils.isNotEmpty(ids)) {
			try {
				List<Long> linkUserIds = itripOrderLinkUserService.getItripOrderLinkUserIdsByOrder();
				Collections.addAll(idsList, ids);
				idsList.removeAll(linkUserIds);
				if (idsList.size() > 0) {
					return DtoUtil.returnFail("所选的常用联系人中有与某项支付有关联的项，无法删除", "100431");
				} else {
					itripSearchService.deleteUserLinkUser(ids);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return DtoUtil.returnFail("删除常用联系人失败", "100432");
			}
			return DtoUtil.returnDataSuccess("删除成功!");
		} else if (null != cruuentUser && EmptyUtils.isNotEmpty(ids)) {
			return DtoUtil.returnFail("请选择要删除的联系人", "100433");
		} else {
			return DtoUtil.returnFail("token失效，请重新登录", "100000");
		}
	}

}
