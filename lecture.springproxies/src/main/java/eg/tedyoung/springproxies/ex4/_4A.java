package eg.tedyoung.springproxies.ex4;

import java.lang.reflect.Proxy;

import org.junit.Test;

import eg.tedyoung.springproxies.Example;

public class _4A extends Example {
	@Test
	public void cacheTest() {
		ExpensiveService target = new ExpensiveServiceImpl();
		
		CacheWrapper wrapper = new CacheWrapper(target);
		
		ExpensiveService proxy = (ExpensiveService) 
			Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ ExpensiveService.class }, wrapper);
		
		
		log.info("Calling expensive lookup...");
		String result = proxy.expensiveLookup();
		log.info("Result = {}\n", result);
		
		log.info("Calling expensive lookup...");
		result = proxy.expensiveLookup();
		log.info("Result = {}\n", result);
	}
}
