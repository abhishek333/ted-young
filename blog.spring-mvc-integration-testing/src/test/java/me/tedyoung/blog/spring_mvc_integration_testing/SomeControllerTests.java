package me.tedyoung.blog.spring_mvc_integration_testing;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring.xml", loader=MockWebApplicationContextLoader.class)
@MockWebApplication(name="some-controller")
public class SomeControllerTests {
	@Autowired
	private DispatcherServlet servlet;
	
	@Autowired
	private SomeRepository repository;
	
	/**
	 * Tests that GET /view?id=0 returns the correct view.
	 * This shows an example of testing the view.
	 */
	@Test
	public void viewTest() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/view");
		request.addParameter("id", "0");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		servlet.service(request, response);
		String results = response.getContentAsString().trim();
		
		Assert.assertEquals("<html><body>Hello World!</body></html>", results);
	}
	
	/**
	 * Tests that POST / creates a new entity.
	 * This shows an example of testing the model.
	 */
	@Test
	public void saveTest() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
		request.addParameter("name", "Ted");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		servlet.service(request, response);
		
		Assert.assertEquals("Ted", repository.find(1).getName());
	}
	
	/**
	 * Tests that POST / creates a new entity.
	 * This shows an example of failing validation.
	 */
	@Test(expected=NestedServletException.class)
	public void saveFailedTest() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
		request.addParameter("name", "");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		servlet.service(request, response);
	}
	
	/**
	 * Tests that GET /secure/view requires authentication.
	 */
	@Test(expected=NestedServletException.class)
	public void secureFailedTest() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/secure/view");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		servlet.service(request, response);
	}
	
	/**
	 * Tests that GET /secure/view with authentication.
	 */
	@Test
	public void secureTest() throws ServletException, IOException {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("ted", "password"));
		
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/secure/view");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		servlet.service(request, response);
		String results = response.getContentAsString().trim();
		
		Assert.assertEquals("<html><body>Hello Secure!</body></html>", results);
	}
}
