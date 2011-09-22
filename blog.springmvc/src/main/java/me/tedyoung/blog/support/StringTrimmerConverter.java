package me.tedyoung.blog.support;

import org.springframework.core.convert.converter.Converter;

public class StringTrimmerConverter implements Converter<String, String> {
	@Override
	public String convert(String source) {
		return source.trim();
	}
}
