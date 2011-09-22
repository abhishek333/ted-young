package me.tedyoung.blog.contentnegotatingresponsebody;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * <p>
 * This component extends the content negation capabilities of {@link ResponseBody}.  
 * In Spring 3.0, {@link ResponseBody} is only affected by the <tt>Accept</tt> header of the request 
 * (see report below).  This is difficult to work with since most browsers do not let the user
 * change the <tt>Accept</tt> header.  This fix makes {@link ResponseBody} pay attention to the 
 * file extension of request url, similar to {@link ContentNegotiatingViewResolver}.
 * </p>
 * <p>
 * Component configuration:
 * <pre>
 * &lt;aop:aspectj-autoproxy/&gt;
 * 
 * &lt;bean class='me.tedyoung.blog.contentnegotatingresponsebody.ContentNegotatingResponseBody'&gt;
 *   &lt;property name='mediaTypes'&gt;
 *     &lt;map&gt;
 *       &lt;entry key="xml" value="application/xml"/&gt;
 *       &lt;entry key="json" value="application/json"/&gt;
 *     &lt;/map&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * @see http://jira.springframework.org/browse/SPR-6993
 */
@Aspect
public class ContentNegotatingResponseBody {
	/**
	 * The LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentNegotatingResponseBody.class);
	
	/**
	 * Derives the request URI.
	 */
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	/**
	 * Maps url extensions to media types.
	 */
	private HashMap<String, String> mediaTypes = new HashMap<String, String>();
	
	/**
	 * Creates a new ContentNegotatingResponseBody.
	 */
	public ContentNegotatingResponseBody() {
	}

	/**
	 * Around advice that extends the behavior of {@link AnnotationMethodHandlerAdapter#handle(HttpServletRequest, HttpServletResponse, Object)}.
	 * Checks to see if the file extension has a media type associated with it.  If so, it provides a modified
	 * <tt>Accept</tt> header, otherwise, it passes the original request unaltered.
	 * @param joinPoint the handle method.
	 * @param request the servlet request.
	 * @param response the servlet response.
	 * @param handler something else used by the handle method.
	 * @return the results of the called method.
	 * @throws Throwable if any exception is thrown.
	 */
	@Around("execution(* org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.handle(..))"
			+ "&& args(request, response, handler)")
	protected Object wrapHandleResponseBody(ProceedingJoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response, Object handler) throws Throwable {
		String extension = getExtensionFromRequest(request);
		
		HttpServletRequest modfiedRequest = request;
		
		String mediaType = mediaTypes.get(extension);
		if (StringUtils.hasText(mediaType)) {
			modfiedRequest = new ModifiedHttpServletRequest(request, mediaType);
			LOGGER.debug("Found url extension {}, changing Accept header to {}.", extension, mediaType);
		}
		else {
			LOGGER.debug("Found unmapped url extension {}, leaving Accept header unaltered.", extension);
		}
		
		return joinPoint.proceed(new Object[]{modfiedRequest, response, handler});
	}
	
	/**
	 * Derives the file extension from the request uri.  The logic for this was derived from
	 * <a href='http://jira.springframework.org/browse/SPR-6993'>SPR-6993</a>
	 * @param request the request.
	 * @return the file extension of the request uri.
	 */
	private String getExtensionFromRequest(HttpServletRequest request) {
		return StringUtils.getFilenameExtension(WebUtils.extractFullFilenameFromUrlPath(urlPathHelper.getRequestUri(request)));
	}
	
	/**
	 * A {@link HttpServletRequest} with a modified Accept header.
	 */
	private static class ModifiedHttpServletRequest extends HttpServletRequestWrapper {
		/**
		 * The accept headers.
		 */
		private Vector<String> acceptHeaders = new Vector<String>(1);
		
		/**
		 * Creates a new ModifiedHttpServletRequest.
		 * @param request the original request.
		 * @param acceptHeader the modified accept header.
		 */
		public ModifiedHttpServletRequest(HttpServletRequest request, String acceptHeader) {
			super(request);
			this.acceptHeaders.add(acceptHeader);
		}
		
		/**
		 * {@inheritDoc}
		 * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
		 */
		@Override
		public String getHeader(String name) {
			if (name.equalsIgnoreCase("accept"))
				return acceptHeaders.firstElement();
			else
				return super.getHeader(name);
		}
		
		/**
		 * {@inheritDoc}
		 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(java.lang.String)
		 */
		@Override
		public Enumeration<?> getHeaders(String name) {
			if (name.equalsIgnoreCase("accept"))
				return acceptHeaders.elements();
			else
				return super.getHeaders(name);
		}
		
		/**
		 * {@inheritDoc}
		 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaderNames()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public Enumeration<?> getHeaderNames() {
			if(super.getHeaderNames() != null) {
		        // Get all header names
		        List<String> names = Collections.list(super.getHeaderNames());

		        // Search for "accept" header
		        boolean contains = false;
		        for(String name: names)
		            if(name.equalsIgnoreCase("accept"))
		                contains = true;
		        
		        // Add Accept header name if does not exist
		        if(!contains)
		            names.add("Accept");
		        
		        return Collections.enumeration(names);
		    }
			else {
				return super.getHeaderNames();
			}
		}
	}

	/**
	 * Gets the mediaTypes.
	 * @return the mediaTypes.
	 * @see #mediaTypes.
	 */
	public HashMap<String, String> getMediaTypes() {
		return mediaTypes;
	}

	/**
	 * Sets the mediaTypes.
	 * @param mediaTypes the new mediaTypes to set.
	 * @see #mediaTypes.
	 */
	public void setMediaTypes(HashMap<String, String> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}
}
