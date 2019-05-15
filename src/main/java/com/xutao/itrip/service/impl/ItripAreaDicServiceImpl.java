package com.xutao.itrip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripAreaDic;
import com.xutao.itrip.mapper.ItripAreaDicMapper;
import com.xutao.itrip.service.ItripAreaDicService;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 
 * @author 许涛
 *
 */
@Service
public class ItripAreaDicServiceImpl implements ItripAreaDicService {
	
	@Resource
	private ItripAreaDicMapper itripAreaDicMapper;
	
	/**
	 * 需求:根据id查询
	 */
	public ItripAreaDic getItripAreaDicById(Long id) throws Exception {
		return itripAreaDicMapper.getItripAreaDicById(id);
	}

	/**
	 * 需求:查询列表
	 */
	public List<ItripAreaDic> getItripAreaDicByListMap(Map<String, Object> param) throws Exception {
		return itripAreaDicMapper.getItripAreaDicListByMap(param);
	}

	/**
	 * 需求:查询总记录数
	 */
	public Integer getItripAreaDicByMap(Map<String, Object> param) throws Exception {
		return itripAreaDicMapper.getItripAreaDicCountByMap(param);
	}

	/**
	 * 需求:添加
	 */
	public Integer ItriptxAddItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
		itripAreaDic.setCreationDate(new Date());
		return itripAreaDicMapper.insertItripAreaDic(itripAreaDic);
	}

	/**
	 * 需求:修改
	 */
	public Integer ItriptxModifyItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
		itripAreaDic.setModifyDate(new Date());
		return itripAreaDicMapper.updateItripAreaDic(itripAreaDic);
	}

	/**
	 * 需求:删除
	 */
	public Integer ItriptxDeleteItripAreaDicById(Long id) throws Exception {
		return itripAreaDicMapper.deleteItripAreaDicById(id);
	}

	/**
	 * 需求:分页
	 */
	public Page<ItripAreaDic> queryItripAreaDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)
			throws Exception {
		Integer total=itripAreaDicMapper.getItripAreaDicCountByMap(param);
		pageNo=EmptyUtils.isEmpty(pageNo)?Constants.DEFAULT_PAGE_NO: pageNo;
		pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
		Page page=new Page(total,pageNo,pageSize);
		param.put("beginPos", page.getBeginPos());
		param.put("pageSize", page.getPageSize());
		List<ItripAreaDic>itripAreaDicsList=itripAreaDicMapper.getItripAreaDicListByMap(param);
		page.setRows(itripAreaDicsList);
		return page;
	}

}
