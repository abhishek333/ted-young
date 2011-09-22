package me.tedyoung.blog.rest;

import me.tedyoung.blog.Article;
import me.tedyoung.blog.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/articles")
public class RestArticleController extends AbstractRestController<Article> {
	@Autowired
	private ArticleService service;

	@Override
	public ArticleService getService() {
		return service;
	}
}
