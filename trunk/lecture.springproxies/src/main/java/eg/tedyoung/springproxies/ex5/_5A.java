package eg.tedyoung.springproxies.ex5;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;

import eg.tedyoung.springproxies.Example;

public class _5A extends Example {
	@Test
	public void processorSwitch() {
		DaytimeProcessor daytimeProcessor = new DaytimeProcessor();
		
		HotSwappableTargetSource targetSource = new HotSwappableTargetSource(daytimeProcessor);
		
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTargetSource(targetSource);
		Processor processorProxy = (Processor) proxyFactoryBean.getObject();
		
		ProcessingService service = new ProcessingService(processorProxy);
		
		service.process("ONE");
		service.process("TWO");
		
		targetSource.swap(new NighttimeProcessor());
		
		service.process("THREE");
		service.process("FOUR");
	}
}
