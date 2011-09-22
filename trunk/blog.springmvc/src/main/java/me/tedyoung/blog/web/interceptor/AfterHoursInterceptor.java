package me.tedyoung.blog.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AfterHoursInterceptor extends HandlerInterceptorAdapter {
	private String redirect;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean openForBusiness = false;  // Lookup from some service, e.g. JMX
		if (openForBusiness) {
			return true;
		}
		else {
			response.sendRedirect(redirect);
			return false;
		}
			
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
