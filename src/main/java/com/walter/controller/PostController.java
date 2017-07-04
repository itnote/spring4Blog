package com.walter.controller;

import com.drew.imaging.ImageProcessingException;
import com.google.api.services.drive.model.File;
import com.walter.config.CustomStringUtils;
import com.walter.model.*;
import com.walter.service.ConfigService;
import com.walter.service.GoogleDriveService;
import com.walter.service.LuceneService;
import com.walter.service.PostService;
import com.walter.util.CRUD;
import com.walter.util.MediaImageMetadata;
import com.walter.util.Message;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yhwang131 on 2016-10-11.
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	/*
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpServletRequest request;
	*/
	@Autowired
	private PostService postService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private LuceneService luceneService;

	@Resource(name = "googleDriveServiceImage")
	private GoogleDriveService googleDriveImageService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPostForm(Model model) {
		model.addAttribute("postVO", new PostVO(true, true));
		model.addAttribute("countryList", configService.getCodeList(new CodeVO("NAT")));
		return "post/postForm";
	}

	@RequestMapping(value = "/register/{post_cd}", method = RequestMethod.GET)
	public String modifyPostForm(@PathVariable int post_cd, Model model) {
		model.addAttribute("postVO", postService.getPost(post_cd));
		model.addAttribute("countryList", configService.getCodeList(new CodeVO("NAT")));
		return "post/postForm";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("postVO") @Valid PostVO postVO, Errors errors) throws IOException {
		if (errors.hasErrors()) return "post/postForm";
		CRUD crud = CRUD.CREATE;
		if (postVO.getPost_cd() != 0 && postService.getPost(postVO.getPost_cd()) != null) crud = CRUD.UPDATE;
		if (crud.equals(CRUD.UPDATE) && !postVO.getDelegate_img().equals(postVO.getNew_delegate_img())) {
			googleDriveImageService.removeFile(postVO.getDelegate_img());
			postVO.setDelegate_img(null);
		}
		if (postVO.getDelegate_img_file().getSize() > 0) {
			File uploadFile = googleDriveImageService.createFile(postVO.getDelegate_img_file());
			postVO.setDelegate_img(uploadFile.getId());
		}
		postService.setPost(postVO, crud);
		luceneService.createIndex(postService.getPostList(new PostSearchVO()));
		return "redirect:" + postVO.getPost_cd();
	}

	@RequestMapping(value = "/{post_cd}", method = RequestMethod.GET)
	public String postView(@PathVariable int post_cd, Model model, HttpServletRequest request)
			throws IOException, ImageProcessingException {
		int currPageNo
				= CustomStringUtils.setDefaultNumber(request.getParameter("currPageNo"), 1);
		PostVO postVO = postService.getPost(post_cd);
		model.addAttribute("currPageNo", currPageNo);
		model.addAttribute("post", postVO);
		if (postVO.getDelegate_img() != null && !postVO.getDelegate_img().isEmpty()) {
			HashMap<String, Object> hashMap = googleDriveImageService.openFile(postVO.getDelegate_img());
			model.addAttribute("image_spec", MediaImageMetadata.getImageSpecString((InputStream)hashMap.get("data")));
		}
		return "post/postView";
	}

	@RequestMapping(value = "/{post_cd}", method = RequestMethod.DELETE)
	public ResponseEntity removePost(@PathVariable int post_cd) throws IOException {
		PostVO postVO = postService.getPost(post_cd);
		if (CustomStringUtils.isNotEmpty(postVO.getDelegate_img())) {
			googleDriveImageService.removeFile(postVO.getDelegate_img());
		}
		luceneService.createIndex(postService.getPostList(new PostSearchVO()));
		return super.createResEntity(new HashMap<String, Object>().put("status", postService.setPost(postVO, CRUD.DELETE)));
	}

	@RequestMapping(value = "/list", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String postList(@ModelAttribute("postSearchVO")PostSearchVO postSearchVO) {
		return gson.toJson(postService.getPostListByPaging(postSearchVO));
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
	public String fileUploadForm() {
		return "drive/fileUpload";
	}

	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	public String imgUpload(@RequestParam("upload")MultipartFile file, Model model, HttpServletRequest request) throws IOException {
		File resultFile = googleDriveImageService.createFile(file);
		model.addAttribute("CKEditorFuncNum",  request.getParameter("CKEditorFuncNum"));
		model.addAttribute("fileURL", "\\/post\\/images\\/" + resultFile.getId());
		return "post/imgUpload";
	}

	@RequestMapping(value = "/dragAndDropUpload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String dragAndDropUpload(@RequestParam("upload")MultipartFile file) throws IOException {
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			File resultFile = googleDriveImageService.createFile(file);
			resultMap.put("uploaded", 1);
			resultMap.put("fileName", resultFile.getName());
			resultMap.put("url", "/post/images/" + resultFile.getId());
		} catch(IOException ioe) {
			ioe.printStackTrace();
			resultMap.put("uploaded", 0);
			resultMap.put("error", new HashMap<String, Object>().put("message", ioe.getMessage()));
		}
		return gson.toJson(resultMap);
	}

	@RequestMapping(value = "/images/{file_id}", method = RequestMethod.GET)
	public void imgView(@PathVariable String file_id, HttpServletResponse response) throws IOException {
		HashMap<String, Object> hashMap = googleDriveImageService.openFile(file_id);
		response.setContentType(hashMap.get("mimeType").toString());
		response.getOutputStream().write(IOUtils.toByteArray((InputStream)hashMap.get("data")));
	}

	@RequestMapping(value = "/comment", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getCommentList(HttpServletRequest request) {
		int postCd = CustomStringUtils.setDefaultNumber(request.getParameter("postCd"), -1);
		return gson.toJson(postService.getComments(postCd));
	}

	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public ResponseEntity registerComment(@ModelAttribute("commentVO")CommentVO commentVO, HttpServletRequest request) {
		commentVO.setIp(request.getRemoteAddr());
		Message msg = postService.setComment(commentVO);
		return resEntity(msg);
	}

	@RequestMapping(value = "/comment", method = RequestMethod.DELETE)
	public ResponseEntity removeComment(@RequestParam("_id")String _id) {
		Message msg = postService.removeComment(_id);
		return resEntity(msg);
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public ResponseEntity registerReply(@ModelAttribute("replyVo")ReplyVO replyVO, HttpServletRequest request) {
		replyVO.setIp(request.getRemoteAddr());
		Message msg = postService.setReply(request.getParameter("_id"), replyVO);
		return resEntity(msg);
	}

	@RequestMapping(value = "/reply", method = RequestMethod.DELETE)
	public ResponseEntity removeReply(@RequestParam("_id")String _id, @RequestParam("index")int index) {
		Message msg = postService.removeReply(_id, index);
		return resEntity(msg);
	}

	private ResponseEntity resEntity(Message msg) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("status", msg == null ? true:false);
		if (msg != null) hashMap.put("message", msg.getText());
		return super.createResEntity(hashMap);
	}
	/*
	@RequestMapping(value = "/http")
	@ResponseBody
	public String http(Model model) {
		ResultVO resultVO = restTemplate.postForObject("http://localhost:3000/rise", new test(2, "ppp"), ResultVO.class);
		return resultVO.toString();
	}

	class test {
		private int key;
		private String value;

		public test (int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	*/

	@RequestMapping(value = "/luceneSearch")
	public ResponseEntity luceneSearch(@RequestParam("searchText") String searchText) throws IOException, ParseException {
		List result = luceneService.searchDataList(PostVO.class, searchText);
		return super.createResEntity(postService.getPostListByLucene(result));
	}
}
