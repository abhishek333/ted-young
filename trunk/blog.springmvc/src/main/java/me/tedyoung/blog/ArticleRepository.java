package me.tedyoung.blog;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository {
	@PersistenceContext
	private EntityManager entityManager;
	
	public ArticleRepository() {
	}
	
	@SuppressWarnings("unchecked")
	public List<Article> find() {
		return entityManager.createQuery("from Article order by id")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Article> find(String title) {
		return entityManager.createQuery("from Article where title like concat('%', :query, '%') order by id")
				.setParameter("query", title)
				.getResultList();
	}
	
	public Article load(long id) {
		return entityManager.find(Article.class, id);
	}
	
	public void persist(Article article) {
		entityManager.persist(article);
	}
	
	public void merge(Article article) {
		entityManager.merge(article);
	}
	
	public void remove(Article article) {
		entityManager.remove(article);
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
