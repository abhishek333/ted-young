package me.tedyoung.blog;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
public class SpringTest {
	private static final String ARTICLES_URL = "http://localhost:8080/springmvc/rest/articles.json";
	private static final String ARTICLE_URL = "http://localhost:8080/springmvc/rest/articles/{id}.json";
	
	
	private Logger logger = Logger.getLogger("TEST");
	
	@Test
	public void restTest() {
		RestTemplate rest = new RestTemplate();
		Article article = rest.getForObject(ARTICLE_URL, Article.class, 1);
		article.setTitle("UPDATED");
		
		rest.put(ARTICLE_URL, article, 1);
		article = rest.getForObject(ARTICLE_URL, Article.class, 1);
		logger.log(Level.SEVERE, article.getTitle());
		
		rest.delete(ARTICLE_URL, 1);
		
		logger.log(Level.SEVERE, rest.getForObject(ARTICLES_URL, LinkedList.class).toString());
		
	}
}
