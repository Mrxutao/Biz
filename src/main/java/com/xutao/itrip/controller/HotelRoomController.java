package com.xutao.itrip.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xutao.itrip.beans.dto.Dto;
import com.xutao.itrip.beans.pojo.ItripHotelRoom;
import com.xutao.itrip.beans.vo.ItripImageVO;
import com.xutao.itrip.beans.vo.ItripLabelDicVO;
import com.xutao.itrip.beans.vo.hotelroom.ItripHotelRoomVO;
import com.xutao.itrip.beans.vo.hotelroom.SearchHotelRoomVO;
import com.xutao.itrip.service.ItripAreaDicService;
import com.xutao.itrip.service.ItripHotelRoomService;
import com.xutao.itrip.service.ItripImageService;
import com.xutao.itrip.service.ItripLabelDicService;
import com.xutao.itrip.utils.DateUtil;
import com.xutao.itrip.utils.DtoUtil;
import com.xutao.itrip.utils.EmptyUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(value = "API", basePath = "/http://api.itrip.com/api")
@RequestMapping(value = "/api/hotelroom")
public class HotelRoomController {

	private Logger logger = Logger.getLogger(HotelRoomController.class);
	@Resource
	private ItripHotelRoomService itripHotelRoomService;
	@Resource
	private ItripImageService itripImageService;
	@Resource
	private ItripAreaDicService itripAreaDicService;
	@Resource
	private ItripLabelDicService ItripLabelDicService;

	@ApiOperation(value = "根据targetId查询酒店房型ID(type=1)", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据酒店房型ID查询酒店房型图片"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100301 : 获取酒店房型图片失败 </p>" + "<p>100302 : 酒店房型id不能为空</p>")
	@RequestMapping(value = "getImgByTargetId/{targetId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Dto<Object> getImgByTargetId(
			@ApiParam(required = true, name = "targetId", value = "酒店房型ID") @PathVariable String targetId) {
		Dto<Object> dto = null;
		if (targetId != null && !"".equals(targetId)) {
			List<ItripImageVO> itripimageVOList = null;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", 1);
			map.put("targetId", targetId);
			try {
				itripimageVOList = itripImageService.getItripImageListByMap(map);
				dto = DtoUtil.returnSuccess("获取酒店图片成功", itripimageVOList);
			} catch (Exception e) {
				dto = DtoUtil.returnFail("获取酒店图片房型失败", "100301");
				e.printStackTrace();
			}
		} else {
			dto = DtoUtil.returnFail("系统不能为空", "100302");
		}
		return dto;

	}

	@ApiOperation(value = "查询酒店房间列表", httpMethod = "POST", protocols = "HTTP", produces = "application", response = Dto.class, notes = "查询酒店房间列表"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100303 : 酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间</p>" + "<p>100304 : 系统异常</p>")
	@ResponseBody
	@RequestMapping(value = "queryHotelRoomByHotel")
	public Dto<List<ItripHotelRoomVO>> queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo) {
		List<List<ItripHotelRoomVO>> queryHotelRoomByHotel = null;
		try {
			Map<String, Object> map = new HashMap();
			if (EmptyUtils.isNotEmpty(vo.getHotelId())) {
				return DtoUtil.returnFail("酒店Id不能为空", "100303");
			}
			if (EmptyUtils.isEmpty(vo.getStartDate()) || EmptyUtils.isEmpty(vo.getEndDate())) {
				return DtoUtil.returnFail("必须填写酒店入住及退房时间", "100303");
			}
			if (EmptyUtils.isNotEmpty(vo.getStartDate()) && EmptyUtils.isNotEmpty(vo.getEndDate())) {
				if (vo.getStartDate().getTime() > vo.getEndDate().getTime()) {
					return DtoUtil.returnFail("入住时间不能大于退房时间", "100303");
				}
				List<Date> dates = DateUtil.getBetweenDates(vo.getStartDate(), vo.getEndDate());
				map.put("timeList", dates);
			}
			vo.setIsHavingBreakfast(EmptyUtils.isEmpty(vo.getIsHavingBreakfast()) ? null : vo.getIsHavingBreakfast());
			vo.setIsBook(EmptyUtils.isEmpty(vo.getIsBook()) ? null : vo.getIsBook());
			vo.setIsTimelyResponse(EmptyUtils.isEmpty(vo.getIsTimelyResponse()) ? null : vo.getIsTimelyResponse());
			vo.setRoomBedTypeId(EmptyUtils.isEmpty(vo.getRoomBedTypeId()) ? null : vo.getRoomBedTypeId());
			vo.setIsCancel(EmptyUtils.isEmpty(vo.getIsCancel()) ? null : vo.getIsCancel());
			vo.setPayType(EmptyUtils.isEmpty(vo.getPayType()) ? null : vo.getPayType());

			map.put("hotelId", vo.getHotelId());
			map.put("IsBook", vo.getIsBook());
			map.put("isHavingBreakfast", vo.getIsHavingBreakfast());
			map.put("isTimelyResponse", vo.getIsTimelyResponse());
			map.put("roomBedTypeId", vo.getRoomBedTypeId());
			map.put("isCancel", vo.getIsCancel());
			map.put("payType", vo.getPayType() == 3 ? null : vo.getPayType());
			List<ItripHotelRoomVO> orginalRoomList = itripHotelRoomService.getItripHotelRoomListByMap(map);
			queryHotelRoomByHotel = new ArrayList();
			for (ItripHotelRoomVO roomVO : orginalRoomList) {
				List<ItripHotelRoomVO> tempList = new ArrayList<ItripHotelRoomVO>();
				tempList.add(roomVO);
				queryHotelRoomByHotel.add(tempList);
			}
			return DtoUtil.returnSuccess("获取成功", queryHotelRoomByHotel);
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("获取失败", "100304");
		}
	}

	@ApiOperation(value = "查询酒店房间床型列表", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "查询酒店床型列表"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>100305 : 获取酒店房间床型失败</p>")
	@ResponseBody
	@RequestMapping(value = "/queryhotelroombed", method = RequestMethod.GET, produces = "application/json")
	public Dto<Object> queryhotelroombed() {
		try {
			List<ItripLabelDicVO> itripLabelDicList = ItripLabelDicService.getItripLabelDicByParentId(new Long(1));
			return DtoUtil.returnSuccess("获取成功!", itripLabelDicList);
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("获取失败!", "100305");
		}
	}
}
