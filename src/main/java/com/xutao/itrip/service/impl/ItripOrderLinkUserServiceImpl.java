package com.xutao.itrip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripOrderLinkUser;
import com.xutao.itrip.beans.vo.order.ItripOrderLinkUserVo;
import com.xutao.itrip.mapper.ItripOrderLinkUserMapper;
import com.xutao.itrip.service.ItripOrderLinkUserService;
@Service
public class ItripOrderLinkUserServiceImpl implements ItripOrderLinkUserService {
	
	@Resource
	private ItripOrderLinkUserMapper itripOrderLinkUserMapper;	
	
	    /**
  	    * 需求:根据id查询
	    */
	    public ItripOrderLinkUser getItripOrderLinkUserById(Long id)throws Exception{
	          return itripOrderLinkUserMapper.getItripOrderLinkUserById(id);
	    }
	    
	    /**
	     * 查询所有订单
	     */
	    public List<ItripOrderLinkUserVo>	getItripOrderLinkUserListByMap(Map<String,Object> param)throws Exception{
	        return itripOrderLinkUserMapper.getItripOrderLinkUserListByMap(param);
	    }
	    
	    /**
	     * 需求: 查询订单总记录数
	     */
	    public Integer getItripOrderLinkUserCountByMap(Map<String,Object> param)throws Exception{
	        return itripOrderLinkUserMapper.getItripOrderLinkUserCountByMap(param);
	    }
	    
	    /**
	     *需求:添加新的订单
	     */
	    public Integer itriptxAddItripOrderLinkUser(ItripOrderLinkUser itripOrderLinkUser)throws Exception{
	            itripOrderLinkUser.setCreationDate(new Date());
	            return itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
	    }
	    
	    /**
	     * 需求:修改订单的信息
	     */
	    public Integer itriptxModifyItripOrderLinkUser(ItripOrderLinkUser itripOrderLinkUser)throws Exception{
	        itripOrderLinkUser.setModifyDate(new Date());
	        return itripOrderLinkUserMapper.updateItripOrderLinkUser(itripOrderLinkUser);
	    }
	    
	    /**
	     * 需求:删除订单信息
	     */
	    public Integer itriptxDeleteItripOrderLinkUserById(Long id)throws Exception{
	        return itripOrderLinkUserMapper.deleteItripOrderLinkUserById(id);
	    }
	    
	    /**
	     * 需求:查询数据
	     */
	    public List<Long> getItripOrderLinkUserIdsByOrder() throws Exception{
	        return itripOrderLinkUserMapper.getItripOrderLinkUserIdsByOrder();
	    }
	}
