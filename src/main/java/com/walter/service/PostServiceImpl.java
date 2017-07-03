package com.walter.service;

import com.walter.config.CustomStringUtils;
import com.walter.dao.CodeDao;
import com.walter.dao.PostDao;
import com.walter.model.*;
import com.walter.repository.CommentRepository;
import com.walter.util.CommonUtil;
import com.walter.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yhwang131 on 2016-10-27.
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao dao;

	@Autowired
	private CommentRepository repository;

	@Autowired
	private MongoOperations mongoOps;

	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm";

	@Override
	public int setPost(PostVO postVO) {
		return dao.setPost(postVO);
	}

	@Override
	public PostVO getPost(int post_cd) {
		PostVO postVO = dao.getPost(post_cd);
		postVO.setDf_reg_dt(CustomStringUtils.dateToString(postVO.getReg_dt(), DATE_FORMAT));
		if (postVO.getMod_dt() != null) {
			postVO.setDf_mod_dt(CustomStringUtils.dateToString(postVO.getMod_dt(), DATE_FORMAT));
		}
		return postVO;
	}

	@Override
	public List<PostVO> getPostList(PostSearchVO postSearchVO) {
		return dao.getPostList(postSearchVO);
	}

	@Override
	public HashMap<String, Object> getPostListByPaging(PostSearchVO postSearchVO) throws IndexOutOfBoundsException {
		PagingVO pagingVO = new PagingVO(postSearchVO.getCurrPageNo(), postSearchVO.getRowsPerPage());
		int offset = (pagingVO.getCurrPageNo() - 1) * pagingVO.getRowsPerPage();
		postSearchVO.setOffset(offset);
		List<PostVO> postVOList = dao.getPostList(postSearchVO);
		List<PostVO> resultList = new ArrayList<>();

		if(postVOList.size() > 0) {
			int limit = offset + pagingVO.getRowsPerPage();
			if(limit > postVOList.size()) limit = postVOList.size();
			resultList = postVOList.subList(offset, limit);
			resultList.stream().forEach(p -> p.setDf_reg_dt(CustomStringUtils.dateToString(p.getReg_dt(), DATE_FORMAT)));
		}
		pagingVO.setNumberOfRows(postVOList.size());
		pagingVO.Paging();

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("postList", resultList);
		hashMap.put("paging", pagingVO);
		return hashMap;
	}

	@Override
	public void setComment(CommentVO commentVO) {
		repository.insert(commentVO);
	}

	@Override
	public CommentVO getCommentById(String _id) {
		return repository.findOne(_id);
	}

	@Override
	public List<CommentVO> getComments(int postCd) {
		return repository.findByPostCd(postCd, new Sort(Sort.Direction.ASC, "_id"));
	}

	@Override
	public Message removeComment(String _id) {
		CommentVO commentVO = repository.findOne(_id);
		Message message = removeValidation(commentVO, commentVO.getLink());
		if (message != null) return message;
		if (commentVO.getReplys().size() > 0) return Message.ERROR_REMOVE_HAVE_REPLY;
		repository.delete(_id);
		return null;
	}

	@Override
	public CommentVO setReply(String _id, ReplyVO replyVO) {
		return repository.insertReply(mongoOps, _id, replyVO);
	}

	@Override
	public Message removeReply(String _id, int index) {
		CommentVO commentVO = repository.findOne(_id);
		Message message = removeValidation(commentVO, commentVO.getReplys().get(index).getLink());
		if (message != null) return message;
		commentVO.getReplys().remove(index);
		repository.removeReply(mongoOps, _id, commentVO.getReplys());
		return null;
	}

	/**
	 * 삭제 대상이 없는 경우, 삭제 권한 유무 체크
	 * @param commentVO
	 * @param link
	 * @return
	 */
	private Message removeValidation(CommentVO commentVO, String link) {
		if (commentVO == null) return Message.ERROR_REMOVE_NOT_EXITST;
		if (CommonUtil.getLoginUserInfo() == null ||
				!link.equals(CommonUtil.getLoginUserInfo().getLink())) {
			return Message.ERROR_REMOVE_PERMISSION;
		}
		return null;
	}
}
