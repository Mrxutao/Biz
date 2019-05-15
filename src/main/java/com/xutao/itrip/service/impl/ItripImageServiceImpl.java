package com.xutao.itrip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripImage;
import com.xutao.itrip.beans.vo.ItripImageVO;
import com.xutao.itrip.mapper.ItripImageMapper;
import com.xutao.itrip.service.ItripImageService;
/**
 * 图片表
 * @author 许涛
 *
 */

@Service
public class ItripImageServiceImpl implements ItripImageService {

	@Resource
	private ItripImageMapper itripImageMapper;

	/**
	 * 需求:根据id查询图片
	 */
	public ItripImage getItripImageById(Long id) throws Exception {
		return itripImageMapper.getItripImageById(id);
	}

	/**
	 * 需求:查询图片列表
	 */
	public List<ItripImageVO> getItripImageListByMap(Map<String, Object> parma) throws Exception {
		return itripImageMapper.getItripImageListByMap(parma);
	}

	/**
	 * 需求:查询图片的总记录数
	 */
	public Integer getItripImageCountByMap(Map<String, Object> param) throws Exception {
		return itripImageMapper.getItripImageCountByMap(param);
	}

	/**
	 * 需求:添加图片
	 */
	public Integer itriptxAddItripImage(ItripImage itripImage) throws Exception {
		itripImage.setCreationDate(new Date());
		return itripImageMapper.insertItripImage(itripImage);
	}

	/**
	 * 需求:修改
	 */
	public Integer itriptxModifItripImage(ItripImage itripImage) throws Exception {
		itripImage.setModifyDate(new Date());
		return itripImageMapper.updateItripImage(itripImage);
	}

	/**
	 * 需求:删除
	 */
	public Integer itriptxDeletItripImage(Long id) throws Exception {
		return itripImageMapper.deleteItripImageById(id);
	}

}
