package com.xutao.itrip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xutao.itrip.beans.pojo.ItripLabelDic;
import com.xutao.itrip.beans.vo.ItripLabelDicVO;
import com.xutao.itrip.mapper.ItripLabelDicMapper;
import com.xutao.itrip.service.ItripLabelDicService;
import com.xutao.itrip.utils.Constants;
import com.xutao.itrip.utils.EmptyUtils;
import com.xutao.itrip.utils.Page;
/**
 * 通用字典表
 * @author 许涛
 *
 */
@Service
public class ItripLabelDicServiceImpl implements ItripLabelDicService {

	@Resource
	private ItripLabelDicMapper itripLabelDicMapper;
	
	/**
	 * 需求:根据id查询标题
	 */
	public ItripLabelDic getItripLabelDicById(Long id) throws Exception {
		return itripLabelDicMapper.getItripLabelDicById(id);
	}

	/**
	 * 需求:查看标题
	 */
	public List<ItripLabelDic> getItripLabeListByMap(Map<String, Object> param) throws Exception {
		return itripLabelDicMapper.getItripLabelDicListByMap(param);
	}

	/**
	 * 需求:查询标题总记录数
	 */
	public Integer getItripLabeCountByMap(Map<String, Object> param) throws Exception {
		return itripLabelDicMapper.getItripLabelDicCountByMap(param);
	}

	/**
	 * 需求:添加标题
	 */
	public Integer itriptxAddItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
		itripLabelDic.setCreationDate(new Date());
		return null;
	}

	/**
	 * 需求:修改标题
	 */
	public Integer itriptxModifItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
		itripLabelDic.setModifyDate(new Date());
		return itripLabelDicMapper.updateItripLabelDic(itripLabelDic);
	}

	/**
	 * 需求:删除标题
	 */
	public Integer itriptxDeleteItripLabelDic(Long id) throws Exception {
		return itripLabelDicMapper.deleteItripLabelDicById(id);
	}

	/**
	 * 需求:根据parentId查询数据字典
	 */
	public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
		return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
	}

	/**
	 * 需求:分页
	 */
	public Page<ItripLabelDic> queryItripLabelDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)
			throws Exception {
		Integer total=itripLabelDicMapper.getItripLabelDicCountByMap(param);
		pageNo=EmptyUtils.isNotEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO :pageNo;
		pageSize=EmptyUtils.isNotEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE :pageSize;
		Page page =new Page(total,pageNo,pageSize);
		List<ItripLabelDic>list=itripLabelDicMapper.getItripLabelDicListByMap(param);
		page.setRows(list);
 		return page;
	}

}
