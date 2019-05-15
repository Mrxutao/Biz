package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripUserLinkUser;
import com.xutao.itrip.utils.Page;

public interface ItripUserLinkUserService {
	
	
	/**
	 * 根据用户id查询
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ItripUserLinkUser>getItripUserLinkUserByUserId(Long userId) throws Exception;
	
	
	
	/**
	 * 需求:添加常用联系人
	 * @param itripUserLinkUser
	 */
	public Integer addUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	/**
	 * 需求:删除常用联系人
	 * @param id
	 */
	public Integer deleteUserLinkUser(Long[] ids)throws Exception;
	
	
	/**
	 * 需求:更新常用联系人	
	 * @param itripUserLinkUser
	 */
	public void updateUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;
	
	
	/**
	 * 需求:根据id查询联系人
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripUserLinkUser getByUserLinkById(Long id) throws Exception;
	
	
	/**
	 * 需求:分页
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<ItripUserLinkUser>queryItripUserLinkUserPageByMap(Map<String,Object>param,Integer pageNo,Integer pageSize)throws Exception;

	public List<ItripUserLinkUser>getItripUserLinkUserListByMap(Map<String,Object>param)throws Exception;
	
	public Integer getItripUserLinkUserCountByMap(Map<String,Object>param)throws Exception;
}
