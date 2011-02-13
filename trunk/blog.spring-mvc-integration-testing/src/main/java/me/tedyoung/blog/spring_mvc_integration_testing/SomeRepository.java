package me.tedyoung.blog.spring_mvc_integration_testing;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class SomeRepository {
	private ArrayList<SomeEntity> entities;
	
	public SomeRepository() {
		entities = new ArrayList<SomeEntity>();
		SomeEntity entity = new SomeEntity();
		entity.setName("World");
		entities.add(entity);
	}
	
	public SomeEntity find(int id) {
		return entities.get(id);
	}

	public void persist(SomeEntity entity) {
		entities.add(entity);
	}
}
