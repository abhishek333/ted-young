package eg.tedyoung.springproxies.ex2;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eg.tedyoung.springproxies.Example;

public class _2B extends Example {
	@Autowired
	PaymentService paymentService;
	
	@Test
	public void paymentProcessorSuccess() throws PaymentException {
		// Create Mock
		PaymentProcessor mock = EasyMock.createStrictMock(PaymentProcessor.class);
		
		// Record expected behavior
		mock.processPayment("CCNUMBER", 12345);
		
		// Execute tests
		EasyMock.replay(mock);
		
		paymentService.setPaymentProcessor(mock);
		
		Assert.assertTrue(paymentService.processPayment("CCNUMBER", 12345));
		
		// Verify actual behavior
		EasyMock.verify(mock);
	}
	
	@Test
	public void paymentProcessorFailure() throws PaymentException {
		// Create Mock
		PaymentProcessor mock = EasyMock.createStrictMock(PaymentProcessor.class);
		
		// Record expected behavior
		mock.processPayment("CCNUMBER", 12345);
		EasyMock.expectLastCall().andThrow(new PaymentException());
		
		// Execute tests
		EasyMock.replay(mock);
		
		paymentService.setPaymentProcessor(mock);
		
		Assert.assertFalse(paymentService.processPayment("CCNUMBER", 12345));
		
		// Verify actual behavior
		EasyMock.verify(mock);
	}
}
