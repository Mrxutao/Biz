package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripLabelDic;
import com.xutao.itrip.beans.vo.ItripLabelDicVO;
import com.xutao.itrip.utils.Page;

public interface ItripLabelDicService {
	
	/**
	 * 根据id查询标题
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripLabelDic getItripLabelDicById(Long id)throws Exception;


	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ItripLabelDic>getItripLabeListByMap(Map<String,Object>param)throws Exception;

	/**
	 * 查询标题的总记录数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripLabeCountByMap(Map<String,Object>param)throws Exception;

	
	/**
	 * 添加标题
	 * @param itripLabelDic
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxAddItripLabelDic(ItripLabelDic itripLabelDic)throws Exception;

	
	/**
	 * 修改标题
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxModifItripLabelDic(ItripLabelDic itripLabelDic)throws Exception;

	/**
	 * 删除标题
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxDeleteItripLabelDic(Long id)throws Exception;
	
	 /**
	  * 分页查询
	  * @param param
	  * @param pageNo
	  * @param pageSize
	  * @return
	  * @throws Exception
	  */
	 public Page<ItripLabelDic> queryItripLabelDicPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception;
	
	/**
	 * 根据parentId查询数据字典
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<ItripLabelDicVO>getItripLabelDicByParentId(Long parentId)throws Exception;
}
