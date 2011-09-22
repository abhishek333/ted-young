package me.tedyoung.blog.session;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("authors/register")
@SessionAttributes("registration")
public class AuthorRegistrationController {
	@RequestMapping("")
	public String startFlow(ModelMap model) {
		model.put("registration", new AuthorRegistrationForm());
		return "redirect:/authors/register/name.html";
	}
	
	@RequestMapping("name")
	public String collectName(@ModelAttribute("registration") AuthorRegistrationForm registration) {
		return "authors/register/name";
	}
	
	@RequestMapping(value="name", method=RequestMethod.PUT)
	public String saveName(@ModelAttribute("registration") @Valid AuthorRegistrationForm registration, BindingResult result) {
		result.recordSuppressedField("email");
		result.recordSuppressedField("phoneNumber");
		if (result.hasErrors())
			return "authors/register/name";
		
		return "redirect:/authors/register/contact.html";
	}

	@RequestMapping("contact")
	public String collectContact(@ModelAttribute("registration") AuthorRegistrationForm registration) {
		return "authors/register/contact";
	}
	
	@RequestMapping(value="contact", method=RequestMethod.PUT)
	public String saveContact(@ModelAttribute("registration") @Valid AuthorRegistrationForm registration, BindingResult result) {
		if (result.hasErrors())
			return "authors/register/contact";
		
		return "redirect:/authors/register/review.html";
	}
	
	@RequestMapping("review")
	public String review(@ModelAttribute("registration") AuthorRegistrationForm registration) {
		return "authors/register/review";
	}
	
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String restartFlow() {
		return "redirect:/authors/register.html";
	}
}
