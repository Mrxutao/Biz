package com.xutao.itrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripAreaDic;
import com.xutao.itrip.beans.pojo.ItripHotel;
import com.xutao.itrip.beans.pojo.ItripLabelDic;
import com.xutao.itrip.beans.vo.hotel.HotelVideoDescVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchPolicyHotelVO;
import com.xutao.itrip.mapper.ItripHotelMapper;
import com.xutao.itrip.service.ItripHotelService;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 酒店表
 * @author 许涛
 *
 */
@Service
public class ItripHotelServiceImpl implements ItripHotelService {

	@Resource
	private ItripHotelMapper itripHotelMapper;

	/**
	 * 需求:根据酒店id查询酒店特色、商圈、酒店名称
	 */
	public HotelVideoDescVO getVideoDescByHotelId(Long id) throws Exception {
		HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();
		List<ItripAreaDic> itripAreaDicList = new ArrayList<>();
		itripAreaDicList = itripHotelMapper.getHotelAreaByHotelId(id);
		List<String> tempList1 = new ArrayList<>();
		for (ItripAreaDic itripAreaDic : itripAreaDicList) {
			tempList1.add(itripAreaDic.getName());
		}
		hotelVideoDescVO.setTradingAreaNameList(tempList1);
		List<ItripLabelDic> itripLabelDicList = new ArrayList<>();
		itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
		List<String> tempList2 = new ArrayList<>();

		for (ItripLabelDic itripLabelDic : itripLabelDicList) {
			tempList2.add(itripLabelDic.getName());
		}
		hotelVideoDescVO.setHotelFeatureList(tempList2);

		hotelVideoDescVO.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());
		return hotelVideoDescVO;
	}

	/**
	 * 需求:根据id查询酒店
	 */
	public ItripHotel getItripHotelById(Long id) throws Exception {
		return itripHotelMapper.getItripHotelById(id);
	}

	/**
	 * 需求:根据地
	 */
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }
	@Override
	public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
		List<ItripLabelDic> itripLabelDicList = new ArrayList<>();
		itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
		ItripSearchDetailsHotelVO vo = new ItripSearchDetailsHotelVO();
		List<ItripSearchDetailsHotelVO> list = new ArrayList<ItripSearchDetailsHotelVO>();
		vo.setName("酒店介绍");
		vo.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
		for (ItripLabelDic itriplabenDic : itripLabelDicList) {
			ItripSearchDetailsHotelVO vo2 = new ItripSearchDetailsHotelVO();
			vo2.setName(itriplabenDic.getName());
			vo2.setDescription(itriplabenDic.getDescription());
			list.add(vo2);
		}
		return list;
	}

	@Override
	public List<ItripHotel> getItripHotelListByMap(Map<String, Object> param) throws Exception {
		return itripHotelMapper.getItripHotelListByMap(param);
	}

	@Override
	public Integer getItripHotelCountByMap(Map<String, Object> param) throws Exception {
		return itripHotelMapper.getItripHotelCountByMap(param);
	}

	@Override
	public Integer itriptxAddItripHotel(ItripHotel itripHotel) throws Exception {
		itripHotel.setCreationDate(new Date());
		return itripHotelMapper.insertItripHotel(itripHotel);
	}

	@Override
	public Integer itriptxModifItripHotel(ItripHotel itripHotel) throws Exception {
		itripHotel.setModifyDate(new Date());
		return itripHotelMapper.updateItripHotel(itripHotel);
	}

	@Override
	public Integer itriptxDeleteItripHotelById(Long id) throws Exception {
		return itripHotelMapper.deleteItripHotelById(id);
	}

	@Override
	public Page<ItripHotel> queryItripHotelPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)
			throws Exception {
		Integer tatol = itripHotelMapper.getItripHotelCountByMap(param);
		pageSize = EmptyUtils.isNotEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
		pageNo = EmptyUtils.isNotEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
		Page page = new Page(pageNo, pageSize, tatol);
		List<ItripHotel> list = itripHotelMapper.getItripHotelListByMap(param);
		page.setRows(list);
		return page;
	}

}
