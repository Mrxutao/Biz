package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripAreaDic;
import com.xutao.itrip.utils.Page;

public interface ItripAreaDicService {
	
	/**
	 * 需求:根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripAreaDic getItripAreaDicById(Long id)throws Exception;

	/**
	 * 需求:查询列表
	 * @param itripAreaDic
	 * @return
	 * @throws Exception
	 */
	public List<ItripAreaDic>getItripAreaDicByListMap(Map<String,Object>param)throws Exception;
	
	/**
	 * 需求:查询总记录数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripAreaDicByMap(Map<String,Object>param)throws Exception;
	
	/**
	 * 需求:添加
	 * @param itripAreaDic
	 * @return
	 * @throws Exception
	 */
	public Integer ItriptxAddItripAreaDic(ItripAreaDic itripAreaDic)throws Exception;

	/**
	 * 需求:修改
	 * @param itripAreaDic
	 * @return
	 * @throws Exception
	 */
	public Integer ItriptxModifyItripAreaDic(ItripAreaDic itripAreaDic)throws Exception;

	
	/**
	 * 需求:删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer ItriptxDeleteItripAreaDicById(Long id)throws Exception;

	
	/**
	 * 需求:分页显示列表
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<ItripAreaDic>queryItripAreaDicPageByMap(Map<String, Object>param,Integer pageNo,Integer pageSize)throws Exception;
}
