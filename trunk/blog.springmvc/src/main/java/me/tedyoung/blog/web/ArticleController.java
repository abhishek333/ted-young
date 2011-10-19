package me.tedyoung.blog.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.validation.Valid;

import me.tedyoung.blog.Article;
import me.tedyoung.blog.ArticleService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService service;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(value="articles", method=RequestMethod.GET)
	@Transactional(readOnly=true)
	public String list(ModelMap model) {
		List<Article> articles = service.find();
		model.put("articles", articles);
		return "articles/list";
	}
	
//	@RequestMapping(value="articles", method=RequestMethod.GET)
//	@Transactional(readOnly=true)
//	public String list(@RequestParam(defaultValue="") String query, ModelMap model) {
//		List<Article> articles = service.find(query);
//		model.put("articles", articles);
//		model.put("query", query);
//		return "articles/list";
//	}
	
	@RequestMapping(value="articles/{id}", method=RequestMethod.GET)
	@Transactional(readOnly=true)
	public String view(@PathVariable long id, ModelMap model) {
		Article article = service.load(id);
		model.put("article", article);
		return "articles/view";
	}
	
	@RequestMapping(value="articles/{id}/form", method=RequestMethod.GET)
	@Transactional(readOnly=true)
	public String viewUpdateForm(@PathVariable long id, ModelMap model) {
		Article article = service.load(id);
		model.put("article", article);
		return "articles/form.update";
	}
	
	@RequestMapping(value="articles/{id}", method=RequestMethod.PUT)
	@Transactional
	public String update(@PathVariable long id, @ModelAttribute("article") Article article, BindingResult result) {
		article.setId(id);
		
		validator.validate(article, result);
		if (result.hasErrors())
			return "articles/form.update";
		
		service.save(article);
		return "redirect:/articles/{id}.html";
	}
	
	@RequestMapping(value="articles/form", method=RequestMethod.GET)
	@Transactional(readOnly=true)
	public String viewInsertForm(ModelMap model) {
		model.put("article", new Article());
		return "articles/form.insert";
	}
	
	@RequestMapping(value="articles", method=RequestMethod.POST)
	@Transactional
	public String insert(@Valid @ModelAttribute("article") Article article, BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return "articles/form.update";
		
		service.save(article);
		
		model.put("message", "inserted");
		return "redirect:/articles/" + article.getId() + ".html";
	}
	
	@RequestMapping(value="articles/{id}", method=RequestMethod.DELETE)
	@Transactional
	public String remove(@PathVariable long id) {
		Article article = service.load(id);
		service.remove(article);
		return "redirect:/articles.html";
	}
	
	@RequestMapping(value="articles/{id}.jpg", method=RequestMethod.GET)
	@Transactional(readOnly=true)
	public void viewImage(@PathVariable long id, OutputStream out) throws IOException {
		Article article = service.load(id);
		IOUtils.copy(service.loadImage(article), out);
	}
	
	@RequestMapping(value="articles/{id}/image", method={RequestMethod.POST, RequestMethod.PUT})
	@Transactional
	public String updateImage(@PathVariable long id, @RequestParam MultipartFile image) throws IOException {
		Article article = service.load(id);
		service.saveImage(article, image.getInputStream());
		return "redirect:/articles/" + article.getId() + ".html";
	}
	
//	@ModelAttribute("message")
//	public String message(@RequestParam(required=false) String message) {
//		return null;
//	}
}
