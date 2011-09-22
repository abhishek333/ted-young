package me.tedyoung.blog.rest;

import java.util.Collection;

import me.tedyoung.blog.EntityService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class AbstractRestController<T> {
	public abstract EntityService<T> getService();
	
	@ResponseBody
	@RequestMapping(value="", method=RequestMethod.GET)
	@Transactional
	public Collection<T> list() {
		return getService().find();
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	@Transactional
	public void insert(@RequestBody T object) {
		getService().save(object);
	}

	@ResponseBody
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	@Transactional
	public T view(@PathVariable long id) {
		return getService().load(id);
	}

	@RequestMapping(value="{id}", method=RequestMethod.PUT)
	@Transactional
	public void update(@PathVariable long id, @RequestBody T object) {
		getService().save(object);
	}

	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	@Transactional
	public void delete(@PathVariable long id) {
		getService().remove(getService().load(id));
	}
}
