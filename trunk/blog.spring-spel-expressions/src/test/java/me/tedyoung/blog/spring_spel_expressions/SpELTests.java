package me.tedyoung.blog.spring_spel_expressions;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Various examples of SpEL expressions.
 */
public class SpELTests {
	@Test
	public void evaluatingAnExpression() {
		// Create a configuration:  auto-grow null fields and empty collections.
		SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);
		
		// Create a parser.
		SpelExpressionParser parser = new SpelExpressionParser(configuration);
		
		// Parse an expression.
		Expression expression = parser.parseExpression(" 'Hello ' + #name ");
		
		// Create an evaluation context.
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		// Expose variables to the script.
		context.setVariable("name", "World");
		
		// Evaluate the expression.
		String value = expression.getValue(context, String.class);
		
		Assert.assertEquals("Hello World", value);
	}
	
	@Test
	public void reusingAnExpression() {
		SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);
		SpelExpressionParser parser = new SpelExpressionParser(configuration);
		
		Expression expression = parser.parseExpression(" #value + 1 ");
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		for (int i = 0; i < 10; i ++) {
			context.setVariable("value", i);
			int result = expression.getValue(context, Integer.class);
			Assert.assertEquals(i + 1, result);
		}
	}
	
	@Test
	public void rootObjects() {
		SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);
		SpelExpressionParser parser = new SpelExpressionParser(configuration);
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		// Set the root object.
		SillyObject object = new SillyObject();
		context.setRootObject(object);
		
		// Parse an expression.
		Expression expression = parser.parseExpression("#root.method() == method() and #root.property == property");
		
		// Evaluate the expression.
		boolean result = expression.getValue(context, Boolean.class);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void templateExpressions() {
		SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);
		SpelExpressionParser parser = new SpelExpressionParser(configuration);
		
		// Create a template expression context.
		ParserContext templateContext = new TemplateParserContext();
		
		// Parse an expression.
		Expression expression = parser.parseExpression("Hello #{#name}", templateContext);
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("name", "World");

		String value = expression.getValue(context, String.class);
		
		Assert.assertEquals("Hello World", value);
	}
}
