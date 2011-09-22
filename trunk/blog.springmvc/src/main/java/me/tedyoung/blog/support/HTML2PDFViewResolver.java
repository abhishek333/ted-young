package me.tedyoung.blog.support;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.xml.sax.InputSource;

public class HTML2PDFViewResolver implements ViewResolver, URIResolver {
	@Autowired
	private ResourceLoader resourceLoader;
	
	private ViewResolver viewResolver;
	
	private String template;
	
	private Templates templates;
	
	@PostConstruct
	public void initialize() throws TransformerConfigurationException, TransformerFactoryConfigurationError, IOException {
		InputStream inputStream = resourceLoader.getResource(template).getInputStream();
		templates = TransformerFactory.newInstance().newTemplates(new StreamSource(inputStream));
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		final View view = viewResolver.resolveViewName(viewName, locale);
		
		return new View() {
			@Override
			public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
				final StringWriter html = new StringWriter();
				
				HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
					@Override
					public PrintWriter getWriter() throws IOException {
						return new PrintWriter(html);
					}
				};
				
				view.render(model, request, wrapper);
				
				HTMLConfiguration config = new HTMLConfiguration();
				config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
				DOMParser parser = new DOMParser(config);
				parser.parse(new InputSource(new StringReader(html.toString())));
				DOMSource xhtml = new DOMSource(parser.getDocument());
				
				FopFactory factory = FopFactory.newInstance();
				factory.setURIResolver(HTML2PDFViewResolver.this);
				Fop fop = factory.newFop(MimeConstants.MIME_PDF, response.getOutputStream());	
				
				response.setHeader("Content-type", MimeConstants.MIME_PDF);
				templates.newTransformer().transform(xhtml, new SAXResult(fop.getDefaultHandler()));
			}
			
			@Override
			public String getContentType() {
				return MimeConstants.MIME_PDF;
			}
		};
	}
	
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		try {
			return new StreamSource(resourceLoader.getResource(href).getFile());
		} 
		catch (IOException e) {
			throw new TransformerException(e);
		}
	}

	public ViewResolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(ViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
