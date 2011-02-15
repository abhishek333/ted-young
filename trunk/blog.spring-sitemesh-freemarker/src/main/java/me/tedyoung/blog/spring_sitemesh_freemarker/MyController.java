package me.tedyoung.blog.spring_sitemesh_freemarker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
	@RequestMapping("*.html")
	public String test() {
		return "sample";
	}
}
