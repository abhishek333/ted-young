package eg.tedyoung.springproxies.ex3;

import java.lang.reflect.Proxy;

import org.junit.Test;

import eg.tedyoung.springproxies.Example;

public class _3 extends Example {
	@Test
	public void authenticationTest() {
		User user = new User();
		
		// If false, the method will NOT be executed.
		user.setAuthenticated(false);
		
		SecureService target = new SecureServiceImpl();
		
		SecureService proxy = (SecureService) 
			Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ SecureService.class }, new SecureWrapper(user, target));
		
		proxy.secureMethod();
		
		log.info("Done.");
	}
}
