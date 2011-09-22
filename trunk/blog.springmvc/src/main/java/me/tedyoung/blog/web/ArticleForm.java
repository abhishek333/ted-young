package me.tedyoung.blog.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import me.tedyoung.blog.Article;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

public class ArticleForm {
	@NotBlank @Size(max=20)
	private String title;
	
	@NotNull @Size(max=1024)
	private String content;

	public ArticleForm() {
	}
	
	public void load(Article article) {
		BeanUtils.copyProperties(article, this);
	}
	
	public void merge(Article article) {
		BeanUtils.copyProperties(this, article);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
