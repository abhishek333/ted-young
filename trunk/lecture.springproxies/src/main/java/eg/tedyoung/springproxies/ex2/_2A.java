package eg.tedyoung.springproxies.ex2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eg.tedyoung.springproxies.Example;

public class _2A extends Example {
	@Autowired
	PaymentService paymentService;
	
	@Test
	public void paymentProcessorSuccess() {
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Assert.assertEquals("CCNUMBER", args[0]);
				Assert.assertEquals(12345, args[1]);
				return null;
			}
		};
		
		PaymentProcessor mock = (PaymentProcessor)
			Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ PaymentProcessor.class }, handler);
		
		paymentService.setPaymentProcessor(mock);
		
		Assert.assertTrue(paymentService.processPayment("CCNUMBER", 12345));
	}
	
	@Test
	public void paymentProcessorFailure() {
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Assert.assertEquals("CCNUMBER", args[0]);
				Assert.assertEquals(12345, args[1]);
				throw new PaymentException();
			}
		};
		
		PaymentProcessor mock = (PaymentProcessor)
			Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ PaymentProcessor.class }, handler);
		
		paymentService.setPaymentProcessor(mock);
		
		Assert.assertFalse(paymentService.processPayment("CCNUMBER", 12345));
	}
}
