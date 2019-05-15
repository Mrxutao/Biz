package com.xutao.itrip.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xutao.itrip.beans.dto.Dto;
import com.xutao.itrip.beans.pojo.ItripAreaDic;
import com.xutao.itrip.beans.pojo.ItripLabelDic;
import com.xutao.itrip.beans.vo.ItripAreaDicVO;
import com.xutao.itrip.beans.vo.ItripImageVO;
import com.xutao.itrip.beans.vo.ItripLabelDicVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchPolicyHotelVO;
import com.xutao.itrip.service.ItripAreaDicService;
import com.xutao.itrip.service.ItripHotelService;
import com.xutao.itrip.service.ItripImageService;
import com.xutao.itrip.service.ItripLabelDicService;
import com.xutao.itrip.utils.DtoUtil;
import com.xutao.itrip.utils.EmptyUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(value = "API", basePath = "/http:api.itrip.com/api")
@RequestMapping(value = "/api/hotel")
public class HotelController {

	private Logger logger = Logger.getLogger(HotelController.class);
	@Resource
	private ItripAreaDicService itripAreaDicService;
	@Resource
	private ItripLabelDicService itripLabelDicService;
	@Resource
	private ItripImageService itripImageService;
	@Resource
	private ItripHotelService itripHotelService;

	/**
	 * 需求:查询热门城市的接口
	 * 
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "查询热门城市", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "查询国内、国外的热门城市(1:国内 2:国外)"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>10201 : hotelId不能为空 </p>" + "<p>10202 : 系统异常,获取失败</p>")
	@ResponseBody
	@RequestMapping(value = "/queryHotelCity/{type}", produces = "application/json", method = RequestMethod.GET)
	public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
		List<ItripAreaDic> itripAreaDics = null;
		List<ItripAreaDicVO> itripAreaDiVOS = null;
		try {
			if (EmptyUtils.isNotEmpty(type)) {
				Map map = new HashMap<>();
				map.put("isHot", 1);
				map.put("isChina", type);
				itripAreaDics = itripAreaDicService.getItripAreaDicByListMap(map);
				if (itripAreaDics != null) {
					itripAreaDiVOS = new ArrayList<>();
					for (ItripAreaDic dic : itripAreaDics) {
						ItripAreaDicVO vo = new ItripAreaDicVO();
						BeanUtils.copyProperties(dic, vo);
						itripAreaDiVOS.add(vo);
					}
				}
			} else {
				DtoUtil.returnFail("type不能为空", "10201");
			}
		} catch (Exception e) {
			DtoUtil.returnFail("系统异常", "10202");
			e.printStackTrace();
		}
		return DtoUtil.returnDataSuccess(itripAreaDiVOS);
	}

	/***
	 * 查询商圈的接口
	 *
	 * @param cityId
	 *            根据城市查询商圈
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查询商圈", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据城市查询商圈"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码：</p>"
			+ "<p>10203 : cityId不能为空 </p>" + "<p>10204 : 系统异常,获取失败</p>")
	@RequestMapping(value = "/querytradearea/{cityId}" + "", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Long cityId) {
		List<ItripAreaDic> itripAreaDics = null;
		List<ItripAreaDicVO> itripAreaDicsVOS = null;
		try {
			if (EmptyUtils.isNotEmpty(cityId)) {
				Map map = new HashMap<>();
				map.put("isTradingArea", 1);
				map.put("parent", "cityId");
				itripAreaDics = itripAreaDicService.getItripAreaDicByListMap(map);
				if (EmptyUtils.isNotEmpty(itripAreaDics)) {
					itripAreaDicsVOS = new ArrayList<>();
					for (ItripAreaDic dic : itripAreaDics) {
						ItripAreaDicVO vo = new ItripAreaDicVO();
						BeanUtils.copyProperties(dic, vo);
						itripAreaDicsVOS.add(vo);
					}
				}
			} else {
				DtoUtil.returnFail("cityId不能为空", "10203");
			}
		} catch (Exception e) {
			DtoUtil.returnFail("系统异常", "10204");
			e.printStackTrace();
		}
		return DtoUtil.returnDataSuccess(itripAreaDicsVOS);
	}

	/***
	 * 查询酒店特色列表
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查询酒店特色列表", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "获取酒店特色(用于查询页列表)"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>错误码: </p>"
			+ "<p>10205: 系统异常,获取失败</p>")
	@RequestMapping(value = "/queryhotelfeature", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Dto<ItripLabelDicVO> queryHotelFeature() {
		List<ItripLabelDic> itripLabelDics = null;
		List<ItripLabelDicVO> itripAreaDicVOs = null;
		try {
			Map param = new HashMap();
			param.put("parentId", 16);
			itripLabelDics = itripLabelDicService.getItripLabeListByMap(param);
			if (EmptyUtils.isNotEmpty(itripLabelDics)) {
				itripAreaDicVOs = new ArrayList();
				for (ItripLabelDic dic : itripLabelDics) {
					ItripLabelDicVO vo = new ItripLabelDicVO();
					BeanUtils.copyProperties(dic, vo);
					itripAreaDicVOs.add(vo);
				}
			}

		} catch (Exception e) {
			DtoUtil.returnFail("系统异常", "10205");
			e.printStackTrace();
		}
		return DtoUtil.returnDataSuccess(itripAreaDicVOs);
	}

	/***
	 * 根据酒店id查询酒店设施 -add by donghai
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据酒店id查询酒店设施", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据酒店id查询酒店设施"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>10206: 酒店id不能为空</p>"
			+ "<p>10207: 系统异常,获取失败</p>")
	@RequestMapping(value = "/queryhotelfacilities/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(
			@ApiParam(required = true, name = "id", value = "酒店ID") @PathVariable Long id) {
		ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
		try {
			if (EmptyUtils.isNotEmpty(id)) {
				itripSearchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
				return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
			} else {
				return DtoUtil.returnFail("酒店id不能空", "10206");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("系统异常", "10207");
		}
	}

	/***
	 * 根据酒店id查询酒店政策 -add by donghai
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET", protocols = "HTTP", produces = "application/json", response = Dto.class, notes = "根据酒店id查询酒店政策"
			+ "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" + "<p>10208: 酒店id不能为空</p>"
			+ "<p>10209: 系统异常,获取失败</p>")
	@RequestMapping(value = "/queryhotelpolicy/{id}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public Dto<ItripSearchFacilitiesHotelVO> queryHotelPolicy(
			@ApiParam(required = true, name = "id", value = "酒店ID") @PathVariable Long id) {
		ItripSearchPolicyHotelVO itripSearchPolicyHotelVO = null;
		try {
			if (EmptyUtils.isNotEmpty(id)) {
				itripSearchPolicyHotelVO = itripHotelService.queryHotelPolicy(id);
				return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
			} else {
				return DtoUtil.returnFail("酒店id不能为空", "10208");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("系统异常,获取失败", "10209");
		}
	}

	/***
     * 根据酒店id查询酒店特色和介绍 -add by donghai
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10210: 酒店id不能为空</p>" +
            "<p>10211: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhoteldetails/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchDetailsHotelVOList = itripHotelService.queryHotelDetails(id);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10210");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10211");
        }
    }

    @ApiOperation(value = "根据targetId查询酒店图片(type=0)", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店ID查询酒店图片" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100212 : 获取酒店图片失败 </p>" +
            "<p>100213 : 酒店id不能为空</p>")
    @RequestMapping(value = "/getimg/{targetId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getImgByTargetId(@ApiParam(required = true, name = "targetId", value = "酒店ID")
                                        @PathVariable String targetId) {
    	Dto<Object>dto=null;
    	if(null!=targetId && !"".equals(targetId)) {
    		List<ItripImageVO>itripImageVOList=null;
    		Map<String,Object>map=new HashMap<String,Object>();
    		map.put("type", "0");
    		map.put("targetId", targetId);
    		try {
    			itripImageVOList=itripImageService.getItripImageListByMap(map);
    			dto=DtoUtil.returnSuccess("获取酒店图片成功", itripImageVOList);
			} catch (Exception e) {
				dto=DtoUtil.returnFail("获取图片失败", "100212");
				e.printStackTrace();
			}
    	}else {
    		dto=DtoUtil.returnFail("获取酒店id不能为空", "100213");
    	}
    	return dto;
    }
    
}
