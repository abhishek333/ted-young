package me.tedyoung.blog.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.template.SimpleHash;

/**
 * Works around a bug in {@link FreeMarkerView} that prevents the object_wrapper setting from being propagated
 * to the entire model.  Without this, it would only be applied to the {@link AllHttpScopesHashModel} and not the
 * related objects contained within that model.
 */
public class FixedFreeMarkerView extends FreeMarkerView {
	/**
	 * Creates a new FixedFreeMarkerView.
	 */
	public FixedFreeMarkerView() {
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.springframework.web.servlet.view.freemarker.FreeMarkerView#buildTemplateModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		SimpleHash hash = super.buildTemplateModel(model, request, response);
		hash.setObjectWrapper(getObjectWrapper());
		return hash;
	}
	
}
