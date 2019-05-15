package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripHotel;
import com.xutao.itrip.beans.vo.hotel.HotelVideoDescVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import com.xutao.itrip.beans.vo.hotel.ItripSearchPolicyHotelVO;
import com.xutao.itrip.utils.Page;

public interface ItripHotelService {
		
	/**
	 * 根据酒店id查询酒店特色、商圈、酒店名称
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public HotelVideoDescVO getVideoDescByHotelId(Long id)throws Exception;
	
	
	/**
	 * 根据id查询酒店
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripHotel getItripHotelById(Long id)throws Exception;
	
	
	/**
	 * 根据酒店的id查询酒店的设施
	 * @return
	 * @throws Exception
	 */
	 public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id)throws Exception;

	
	/**
	 * 根据地酒店的id查询酒店的政策
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripSearchPolicyHotelVO queryHotelPolicy(Long id)throws Exception;
	
	
	/**
	 * 根据酒店的id查询酒店的特色和介绍
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<ItripSearchDetailsHotelVO>queryHotelDetails(Long id)throws Exception;
	
	/**
	 * 查询酒店列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ItripHotel>getItripHotelListByMap(Map<String,Object>param)throws Exception;
	
	/**
	 * 查询酒店总记录数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripHotelCountByMap(Map<String,Object>param)throws Exception;
	
	/**
	 * 添加酒店
	 * @param itripHotel
	 * @return
	 */
	public Integer itriptxAddItripHotel(ItripHotel itripHotel)throws Exception;

	
	/**
	 * 修改酒店
	 * @param itripHotel
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxModifItripHotel(ItripHotel itripHotel)throws Exception;
	
	/**
	 * 根据Id删除酒店
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxDeleteItripHotelById(Long id)throws Exception;
	
	/**
	 * 查询酒店列表，以分页的展示。
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<ItripHotel>queryItripHotelPageByMap(Map<String,Object>param,Integer pageNo,Integer pageSize)throws Exception;
}	
