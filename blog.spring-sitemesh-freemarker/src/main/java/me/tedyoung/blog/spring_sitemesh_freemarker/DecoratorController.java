package me.tedyoung.blog.spring_sitemesh_freemarker;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles decorator requests from SiteMesh.
 */
@Controller
public class DecoratorController {
	/**
	 * Map all SiteMesh decorator requests.  Note that we use the pattern
	 * \\w+ to prevent an outside source from getting access to files we might
	 * not want made available.
	 */
	@RequestMapping("decorator/{name:\\w+}.ftl")
	public String decorator(@PathVariable String name, ModelMap map) {
		// Make the current date available to our templates.
		// We can put anything else we want in here.
		map.put("now", new Date());
		return name;
	}
}
