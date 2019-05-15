package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripImage;
import com.xutao.itrip.beans.vo.ItripImageVO;

public interface ItripImageService {
	
	/**
	 * 根据id查询图片
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripImage getItripImageById(Long id)throws Exception;

	/**
	 * 查询图片列表
	 * @param parma
	 * @return
	 * @throws Exception
	 */
	public List<ItripImageVO>getItripImageListByMap(Map<String,Object>parma)throws Exception;
	
	
	/**
	 * 查询图片的总记录数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripImageCountByMap(Map<String,Object>param)throws Exception;
	
	
	/**
	 *添加图片 
	 * @param itripImage
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxAddItripImage(ItripImage itripImage)throws Exception;
	
	
	/**
	 * 修改图片
	 * @param itripImage
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxModifItripImage(ItripImage itripImage)throws Exception;
	
	/**
	 * 删除图片
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxDeletItripImage(Long id)throws Exception;
	
	
}
