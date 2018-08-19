package com.walter.dao;

import com.walter.model.LuceneIndexVO;
import com.walter.model.PostSearchVO;
import com.walter.model.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yhwang131 on 2016-10-12.
 */
@Component
@Mapper
public interface PostDao {
	int insPost(PostVO postVO);
	int modPost(PostVO postVO);
	int delPost(int post_cd);
	PostVO getPost(int post_cd);
	List<PostVO> getPostList(PostSearchVO postSearchVO);
	List<PostVO> getPostListByLucene(List<LuceneIndexVO> list);
}
