package eg.tedyoung.springproxies.ex4;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eg.tedyoung.springproxies.Example;

public class _4B extends Example {
	@Autowired
	ExpensiveService expensiveService;
	
	@Test
	public void cacheTest() {
		// Remember to enable annotations...
		
		log.info("Calling expensive lookup...");
		String result = expensiveService.expensiveLookup();
		log.info("Result = {}\n", result);
		
		log.info("Calling expensive lookup...");
		result = expensiveService.expensiveLookup();
		log.info("Result = {}\n", result);
	}
}
