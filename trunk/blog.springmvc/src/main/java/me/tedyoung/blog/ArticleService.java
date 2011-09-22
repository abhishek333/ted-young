package me.tedyoung.blog;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService implements EntityService<Article> {
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	public ArticleService() {
	}

	@Override
	public List<Article> find() {
		return articleRepository.find();
	}

	public List<Article> find(String title) {
		return articleRepository.find(title);
	}

	@Override
	public Article load(long id) {
		return articleRepository.load(id);
	}
	
	@Override
	public void save(Article article) {
		if (article.getId() == null)
			articleRepository.persist(article);
		else
			articleRepository.merge(article);
	}

	@Override
	public void remove(Article article) {
		articleRepository.remove(article);
		imageRepository.deleteImage(article.getId());
	}
	
	public InputStream loadImage(Article article) throws FileNotFoundException {
		return imageRepository.loadImage(article.getId());
	}
	
	public BufferedImage loadBufferedImage(Article article) throws FileNotFoundException, IOException {
		return imageRepository.loadBufferedImage(article.getId());
	}

	public void saveImage(Article article, InputStream source) throws IOException {
		imageRepository.saveImage(article.getId(), source);
		article.setImagePresent(true);
	}

	public void setArticleRepository(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public void setImageRepository(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
}
