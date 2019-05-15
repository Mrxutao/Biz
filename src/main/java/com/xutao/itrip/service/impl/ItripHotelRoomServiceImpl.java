package com.xutao.itrip.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripHotelRoom;
import com.xutao.itrip.beans.vo.hotelroom.ItripHotelRoomVO;
import com.xutao.itrip.mapper.ItripHotelRoomMapper;
import com.xutao.itrip.service.ItripHotelRoomService;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 酒店房间
 * @author 许涛
 *
 */
@Service
public class ItripHotelRoomServiceImpl implements ItripHotelRoomService {
	
	@Resource
	private ItripHotelRoomMapper itripHotelRoomMapper;
	
	/**
	 * 需求:根据id查询房间名称
	 */
	public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception {
		return itripHotelRoomMapper.getItripHotelRoomById(id);
	}

	/**
	 * 需求:查询房间列表
	 */
	public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception {
		return itripHotelRoomMapper.getItripHotelRoomListByMap(param);
	}

	/**
	 * 需求:查询房间的总记录数
	 */
	public Integer getItripHotelRoomCountByMap(Map<String, Object> param) throws Exception {
		return itripHotelRoomMapper.getItripHotelRoomCountByMap(param);
	}

	/**
	 * 需求:添加房间
	 */
	public Integer itriptxAddItripHotelRoom(ItripHotelRoom itripHotelRoom) throws Exception {
		return itripHotelRoomMapper.insertItripHotelRoom(itripHotelRoom);
	}

	/**
	 * 需求:删除房间
	 */
	public Integer itriptxDeleteItripHotelRoomById(Long id) throws Exception {
		return itripHotelRoomMapper.deleteItripHotelRoomById(id);
	}

	/**
	 * 需求:查询房间，以分页展示
	 */
	public Page<ItripHotelRoomVO> queryItripHotelRoomPageByMap(Map<String, Object> param, Integer pageNo,
			Integer pageSize) throws Exception {
		Integer tatol=itripHotelRoomMapper.getItripHotelRoomCountByMap(param);
 		pageNo=EmptyUtils.isNotEmpty(pageNo) ?Constants.DEFAULT_PAGE_NO :pageNo;
 		pageSize=EmptyUtils.isNotEmpty(pageSize) ?Constants.DEFAULT_PAGE_SIZE :pageSize;
 		Page page=new Page(pageNo,pageSize,tatol);
 		List<ItripHotelRoomVO>list=itripHotelRoomMapper.getItripHotelRoomListByMap(param);
 		page.setRows(list);
		return page;
	}

}
