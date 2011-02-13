package me.tedyoung.blog.spring_mvc_integration_testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
	@Autowired
	private SomeRepository repository;
	
	public SomeService() {
	}
	
	public SomeEntity find(int id) {
		return repository.find(id);
	}

	public void persist(SomeEntity entity) {
		repository.persist(entity);
	}
}
