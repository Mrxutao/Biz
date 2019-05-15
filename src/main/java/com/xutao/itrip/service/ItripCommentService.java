package com.xutao.itrip.service;

import java.util.List;
import java.util.Map;

import com.xutao.itrip.beans.pojo.ItripComment;
import com.xutao.itrip.beans.pojo.ItripImage;
import com.xutao.itrip.beans.vo.comment.ItripListCommentVO;
import com.xutao.itrip.beans.vo.comment.ItripScoreCommentVO;
import com.xutao.itrip.utils.Page;
/**
 * 评论接口
 * @author 许涛
 *
 */
public interface ItripCommentService {

	/**
	 * 根据id查询评论
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripComment getItripCommentById(Long id) throws Exception;

	/**
	 * 查询评论列表
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param) throws Exception;

	/**
	 * 查询评论总记录数
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception;

	/**
	 * 添加点评
	 * 
	 * @param itripComment
	 * @param itripImages
	 * @return
	 * @throws Exception
	 */
	public boolean itriptxAddItripComment(ItripComment itripComment, List<ItripImage> itripImages) throws Exception;

	/**
	 * 修改评论
	 * 
	 * @param itripComment
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxModifyItripComment(ItripComment itripComment) throws Exception;

	/**
	 * 删除评论
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer itriptxDeleteItripCommentById(Long id) throws Exception;

	/**
	 * 查询评论，以分页展示。
	 * 
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo,
			Integer pageSize) throws Exception;

	/**
	 * 根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分-add by donghai
	 * 
	 * @param hotelId
	 * @return
	 * @throws Exception
	 */
	public ItripScoreCommentVO getAvgAndTotalScore(Long hotelId) throws Exception;
}
