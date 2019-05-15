package com.xutao.itrip.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripUserLinkUser;
import com.xutao.itrip.mapper.ItripUserLinkUserMapper;
import com.xutao.itrip.service.ItripUserLinkUserService;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 用户联系人表
 * @author 许涛
 *
 */
@Service
public class ItripUserLinkUserServiceImpl implements ItripUserLinkUserService {

	@Autowired
	private ItripUserLinkUserMapper itripUserLinkUserMapper;

	/**
	 * 需求:根据id查询用户
	 */
	public List<ItripUserLinkUser> getItripUserLinkUserByUserId(Long userId) throws Exception {
		return itripUserLinkUserMapper.getItripUserLinkUserListByUserId(userId);
	}

	/**
	 * 需求:添加联系人
	 */
	public Integer addUserLinkUser(ItripUserLinkUser itripUserLinkUser) throws Exception {
		return itripUserLinkUserMapper.insertItripUserLinkUser(itripUserLinkUser);
	}

	/**
	 * 需求:批量删除联系人
	 */
	public Integer deleteUserLinkUser(Long[] ids) throws Exception {
		return itripUserLinkUserMapper.deleteItripUserLinkUserByIds(ids);
	}

	/**
	 * 需求:更新联系人
	 */
	public void updateUserLinkUser(ItripUserLinkUser itripUserLinkUser) throws Exception {
		itripUserLinkUserMapper.updateItripUserLinkUser(itripUserLinkUser);
	}

	/**
	 * 需求:根据id查询
	 */
	public ItripUserLinkUser getByUserLinkById(Long id) throws Exception {
		return itripUserLinkUserMapper.getItripUserLinkUserById(id);
	}

	/**
	 * 需求:
	 */
	public Page<ItripUserLinkUser> queryItripUserLinkUserPageByMap(Map<String, Object> param, Integer pageNo,
			Integer pageSize) throws Exception {
		Integer total = itripUserLinkUserMapper.getItripUserLinkUserCountByMap(param);
		pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
		pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
		Page page = new Page(pageNo, pageSize, total);
		param.put("beginPos", page.getBeginPos());
		param.put("pageSize", page.getPageSize());
		List<ItripUserLinkUser> itripUserLinkUserList = itripUserLinkUserMapper.getItripUserLinkUserListByMap(param);
		page.setRows(itripUserLinkUserList);
		return page;
	}

	/**
	 * 需求:查询
	 */
	public List<ItripUserLinkUser> getItripUserLinkUserListByMap(Map<String, Object> param) throws Exception {
		List<ItripUserLinkUser> list = itripUserLinkUserMapper.getItripUserLinkUserListByMap(param);
		return list;
	}

	/**
	 * 需求:查询总记录数
	 */
	public Integer getItripUserLinkUserCountByMap(Map<String, Object> param) throws Exception {
		return itripUserLinkUserMapper.getItripUserLinkUserCountByMap(param);
	}

}
