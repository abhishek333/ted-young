package eg.tedyoung.springproxies.ex7;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eg.tedyoung.springproxies.Example;

public class _7 extends Example {
	@Autowired
	TracedService service;
	
	@Test
	public void tracingTest() {
		service.methodOne();
		service.methodTwo();
	}
}
