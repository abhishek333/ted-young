package me.tedyoung.blog.spring_mvc_integration_testing;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SomeController {
	@Autowired
	private SomeService service;
	
	public SomeController() {
	}
	
	@RequestMapping("view")
	public String view(@RequestParam int id, ModelMap model) {
		model.put("entity", service.find(id));
		return "view";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String insert(@Valid SomeEntity entity, ModelMap model) {
		service.persist(entity);
		model.put("entity", entity);
		return "view";
	}
	
	@RequestMapping("/secure/view")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String secureView(ModelMap model) {
		SomeEntity entity = new SomeEntity();
		entity.setName("Secure");
		model.put("entity", entity);
		return "view";
	}
}
