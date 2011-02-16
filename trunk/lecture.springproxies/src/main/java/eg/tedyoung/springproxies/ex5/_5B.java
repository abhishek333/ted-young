package eg.tedyoung.springproxies.ex5;

import org.junit.Test;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;

import eg.tedyoung.springproxies.Example;

public class _5B extends Example {
	@Autowired
	HotSwappableTargetSource processorSwapper;
	
	@Autowired
	ProcessingService service;
	
	@Test
	public void processorSwitch() {
		service.process("ONE");
		service.process("TWO");
		
		processorSwapper.swap(new NighttimeProcessor());
		
		service.process("THREE");
		service.process("FOUR");
	}
}
