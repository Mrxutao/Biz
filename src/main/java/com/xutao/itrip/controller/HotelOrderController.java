package com.xutao.itrip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xutao.itrip.beans.dto.Dto;
import com.xutao.itrip.beans.pojo.ItripHotelOrder;
import com.xutao.itrip.beans.pojo.ItripHotelRoom;
import com.xutao.itrip.beans.pojo.ItripUser;
import com.xutao.itrip.beans.vo.order.ItripListHotelOrderVO;
import com.xutao.itrip.beans.vo.order.ItripModifyHotelOrderVO;
import com.xutao.itrip.beans.vo.order.ItripOrderLinkUserVo;
import com.xutao.itrip.beans.vo.order.ItripPersonalHotelOrderVO;
import com.xutao.itrip.beans.vo.order.ItripPersonalOrderRoomVO;
import com.xutao.itrip.beans.vo.order.ItripSearchOrderVO;
import com.xutao.itrip.mapper.ItripHotelRoomMapper;
import com.xutao.itrip.service.ItripHotelOrderService;
import com.xutao.itrip.service.ItripHotelRoomService;
import com.xutao.itrip.service.ItripOrderLinkUserService;
import com.xutao.itrip.utils.DtoUtil;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
import com.xutao.itrip.utils.SystemConfig;
import com.xutao.itrip.utils.ValidationToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/hotelorder")
public class HotelOrderController {

	private Logger logger = Logger.getLogger(HotelOrderController.class);
	@Autowired
	private ItripHotelOrderService itripHotelOrderService;
	@Autowired
	private ValidationToken validationToken;
	@Resource
	private SystemConfig systemConfig;
	@Autowired
	private ItripOrderLinkUserService itripOrderLinkUserService;
	@Autowired
	private ItripHotelRoomService itripHotelRoomService;

	@ApiOperation(value = "根据个人订单列表，并分页显示", httpMethod = "POST", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据条件查询个人订单列表，并分页显示"
			+ "<p>订单类型(orderType)（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单）：</p>"
			+ "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" + "<p>对于页面tab条件：</p>"
			+ "<p>全部订单（orderStatus：-1）</p>" + "<p>未出行（orderStatus：2）</p>" + "<p>待付款（orderStatus：0）</p>"
			+ "<p>待评论（orderStatus：3）</p>" + "<p>已取消（orderStatus：1）</p>"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100501 : 请传递参数：orderType </p>" + "<p>100502 : 请传递参数：orderStatus </p>" + "<p>100503 : 获取个人订单列表错误 </p>"
			+ "<p>100000 : token失效，请重登录 </p>")
	@RequestMapping(value = "/getPersonalOrderList", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Dto<Page<ItripListHotelOrderVO>> getPersonalOrderList(@RequestBody ItripSearchOrderVO itripSearchOrderVO,
			HttpServletRequest request) {
		Integer orderType = itripSearchOrderVO.getOrderType();
		Integer orderStatus = itripSearchOrderVO.getOrderStatus();
		Dto<Page<ItripListHotelOrderVO>> dto = null;
		String tokenString = request.getHeader("token");
		ItripUser currentUser = validationToken.getCurrentUser(tokenString);
		if (currentUser != null) {
			if (orderType == null) {
				return DtoUtil.returnFail("请传递参数   :orderType", "100501");
			}
			if (orderStatus == null) {
				return DtoUtil.returnFail("请传递参数   :orderStatus", "100502");
			}

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("orderType", orderType == -1 ? null : orderType);
			param.put("orderStatus", orderStatus == -1 ? null : orderStatus);
			param.put("userId", currentUser.getId());
			param.put("orderNo", itripSearchOrderVO.getOrderNo());
			param.put("linkName", itripSearchOrderVO.getLinkUserName());
			param.put("startDate", itripSearchOrderVO.getStartDate());
			param.put("endDate", itripSearchOrderVO.getEndDate());
			try {
				Page page = itripHotelOrderService.queryOrderPageByMap(param, itripSearchOrderVO.getPageNo(),
						itripSearchOrderVO.getPageSize());
				dto = DtoUtil.returnSuccess("获取个人订单成功", page);
			} catch (Exception e) {
				e.printStackTrace();
				dto = DtoUtil.returnFail("获取个人订单失败", "100503");
			}
		} else {
			dto = DtoUtil.returnFail("token失效,请重新登录", "100000");
		}
		return dto;
	}

	@ApiOperation(value = "根据订单ID查看个人订单详情", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据订单ID查看个人订单详情"
			+ "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" + "<p>订单流程：</p>"
			+ "<p>订单状态(0：待支付 2:支付成功 3:已消费 4:已点评)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"支付成功\",\"4\":\"入住\",\"5\":\"订单点评\",\"6\":\"订单完成\"}</p>"
			+ "<p>订单状态(1:已取消)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"订单取消\"}</p>"
			+ "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100525 : 请传递参数：orderId </p>" + "<p>100526 : 没有相关订单信息 </p>" + "<p>100527 : 获取个人订单信息错误 </p>"
			+ "<p>100000 : token失效，请重登录 </p>")
	@RequestMapping(value = "/getpersonalorderinfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Dto<Object> getPersonalOrderInfo(
			@ApiParam(required = true, name = "orderId", value = "订单ID") @PathVariable String orderId,
			HttpServletRequest request) {
		logger.debug("orderId : " + orderId);
		Dto<Object> dto = null;
		String tokenString = request.getHeader("token");
		logger.debug("token name is from header : " + tokenString);
		ItripUser currentUser = validationToken.getCurrentUser(tokenString);
		if (null != currentUser) {
			if (null == orderId || "".equals(orderId)) {
				return DtoUtil.returnFail("请传递参数：orderId", "100525");
			}
			try {
				ItripHotelOrder hotelOrder = itripHotelOrderService.getItripHotelOrderById(Long.valueOf(orderId));
				if (null != hotelOrder) {
					ItripPersonalHotelOrderVO itripPersonalHotelOrderVO = new ItripPersonalHotelOrderVO();
					itripPersonalHotelOrderVO.setId(hotelOrder.getId());
					itripPersonalHotelOrderVO.setBookType(hotelOrder.getBookType());
					itripPersonalHotelOrderVO.setCreationDate(hotelOrder.getCreationDate());
					itripPersonalHotelOrderVO.setOrderNo(hotelOrder.getOrderNo());
					// 查询预订房间的信息
					ItripHotelRoom room = itripHotelRoomService.getItripHotelRoomById(hotelOrder.getRoomId());
					if (EmptyUtils.isNotEmpty(room)) {
						itripPersonalHotelOrderVO.setRoomPayType(room.getPayType());
					}
					Integer orderStatus = hotelOrder.getOrderStatus();
					itripPersonalHotelOrderVO.setOrderStatus(orderStatus);
					// 订单状态（0：待支付 1:已取消 2:支付成功 3:已消费 4:已点评）
					// {"1":"订单提交","2":"订单支付","3":"支付成功","4":"入住","5":"订单点评","6":"订单完成"}
					// {"1":"订单提交","2":"订单支付","3":"订单取消"}
					if (orderStatus == 1) {
						itripPersonalHotelOrderVO
								.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessCancel()));
						itripPersonalHotelOrderVO.setProcessNode("3");
					} else if (orderStatus == 0) {
						itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
						itripPersonalHotelOrderVO.setProcessNode("2");// 订单支付
					} else if (orderStatus == 2) {
						itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
						itripPersonalHotelOrderVO.setProcessNode("3");// 支付成功（未出行）
					} else if (orderStatus == 3) {
						itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
						itripPersonalHotelOrderVO.setProcessNode("5");// 订单点评
					} else if (orderStatus == 4) {
						itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
						itripPersonalHotelOrderVO.setProcessNode("6");// 订单完成
					} else {
						itripPersonalHotelOrderVO.setOrderProcess(null);
						itripPersonalHotelOrderVO.setProcessNode(null);
					}
					itripPersonalHotelOrderVO.setPayAmount(hotelOrder.getPayAmount());
					itripPersonalHotelOrderVO.setPayType(hotelOrder.getPayType());
					itripPersonalHotelOrderVO.setNoticePhone(hotelOrder.getNoticePhone());
					dto = DtoUtil.returnSuccess("获取个人订单信息成功", itripPersonalHotelOrderVO);
				} else {
					dto = DtoUtil.returnFail("没有相关订单信息", "100526");
				}
			} catch (Exception e) {
				e.printStackTrace();
				dto = DtoUtil.returnFail("获取个人订单信息错误", "100527");
			}
		} else {
			dto = DtoUtil.returnFail("token失效，请重登录", "100000");
		}
		return dto;
	}

	@ApiOperation(value = "根据订单ID查看个人订单详情-房型相关信息", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据订单ID查看个人订单详情-房型相关信息"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100529 : 请传递参数：orderId </p>" + "<p>100530 : 没有相关订单房型信息 </p>" + "<p>100531 : 获取个人订单房型信息错误 </p>"
			+ "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" + "<p>100000 : token失效，请重登录 </p>")
	@RequestMapping(value = "/getpersonalorderroominfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Dto<Object> getPersonalOrderRoomInfo(
			@ApiParam(required = true, name = "orderId", value = "订单Id") @PathVariable String orderId,
			HttpServletRequest request) {
		logger.debug("orderId : " + orderId);
		Dto<Object> dto = null;
		String tokenString = request.getHeader("token");
		logger.debug("token name is from header : " + tokenString);
		ItripUser currentUser = validationToken.getCurrentUser(tokenString);
		if (null != currentUser) {
			if (null == orderId || "".equals(orderId)) {
				return DtoUtil.returnFail("请传递参数：orderId", "100529");
			}
			try {
				ItripPersonalOrderRoomVO vo = itripHotelOrderService
						.getItripHotelOrderRoomInfoById(Long.valueOf(orderId));
				if (null != vo) {
					dto = DtoUtil.returnSuccess("获取个人订单房型信息成功", vo);
				} else {
					dto = DtoUtil.returnFail("没有相关订单房型信息", "100530");
				}
			} catch (Exception e) {
				e.printStackTrace();
				dto = DtoUtil.returnFail("获取个人订单房型信息错误", "100531");
			}
		} else {
			dto = DtoUtil.returnFail("token失效，请重登录", "100000");
		}
		return dto;
	}

	@ApiOperation(value = "根据订单ID获取订单信息", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据订单ID获取订单信息"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100533 : 没有查询到相应订单 </p>" + "<p>100534 : 系统异常 </p>")
	@ResponseBody
	public Dto<Object> queryOrderById(
			@ApiParam(required = true, name = "orderId", value = "订单ID") @PathVariable Long orderId,
			HttpServletRequest request) {

		ItripModifyHotelOrderVO itripModifyHotelOrderVO = null;
		try {
			String tokenString = request.getHeader("token");
			ItripUser currentUser = validationToken.getCurrentUser(tokenString);
			if (EmptyUtils.isEmpty(currentUser)) {
				return DtoUtil.returnFail("token失效，请重新登录!", "100000");
			}
			ItripHotelOrder order = itripHotelOrderService.getItripHotelOrderById(orderId);
			if (EmptyUtils.isEmpty(order)) {
				return DtoUtil.returnFail("100533", "没有查询到相应订单");
			}
			itripModifyHotelOrderVO = new ItripModifyHotelOrderVO();
			BeanUtils.copyProperties(order, itripModifyHotelOrderVO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", order.getId());
			List<ItripOrderLinkUserVo> itripOderLinkUserVo = itripOrderLinkUserService
					.getItripOrderLinkUserListByMap(map);
			itripModifyHotelOrderVO.setItripOrderLinkUserList(itripOderLinkUserVo);
			return DtoUtil.returnSuccess("获取订单成功!", itripModifyHotelOrderVO);
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("获取订单失败", "100534");
		}
	}
}
