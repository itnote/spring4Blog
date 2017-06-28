package com.walter.service;

import com.walter.model.CategoryVO;
import com.walter.model.CodeVO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yhwang131 on 2017-05-23.
 */
public interface ConfigService {
	List<CategoryVO> getCategoryList();
	CategoryVO getCategoryItemByCd(int category_cd);
	HashMap insCategoryItem(CategoryVO categoryVO);
	HashMap modCategoryItem(CategoryVO categoryVO, String targetAttribute);
	HashMap delCategoryItem(int category_cd);

	List<CodeVO> getCodeList(CodeVO codeVO);
}
