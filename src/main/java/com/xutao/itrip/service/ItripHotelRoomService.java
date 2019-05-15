package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripHotelRoom;
import com.xutao.itrip.beans.vo.hotelroom.ItripHotelRoomVO;
import com.xutao.itrip.utils.Page;

public interface ItripHotelRoomService {

	/**
	 * 根据id查询酒店房间
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception;

	/**
	 * 查询酒店房型列表
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception;

	/**
	 * 查询酒店房型总记录数
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripHotelRoomCountByMap(Map<String, Object> param) throws Exception;

	/**
	 * 添加酒店房型
	 * 
	 * @param itripHotelRoom
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxAddItripHotelRoom(ItripHotelRoom itripHotelRoom) throws Exception;
	
	/**
	 * 根据Id删除酒店房型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxDeleteItripHotelRoomById(Long id) throws Exception;
	
	/**
	 * 查询酒店房型，以分页显示。
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<ItripHotelRoomVO> queryItripHotelRoomPageByMap(Map<String, Object> param, Integer pageNo,
			Integer pageSize) throws Exception;
}
