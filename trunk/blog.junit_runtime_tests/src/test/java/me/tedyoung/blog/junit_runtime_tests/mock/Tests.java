package me.tedyoung.blog.junit_runtime_tests.mock;

import java.math.BigDecimal;

import me.tedyoung.blog.junit_runtime_tests.mocks.MockingDynamicRunner;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockingDynamicRunner.class)
public class Tests {
	@Test
	public void chargeCreditCardTest(PaymentProcessor paymentProcessor, CreditCard creditCard) {
		// Teach this mock how to behave, what to expect.
		paymentProcessor.charge(creditCard, new BigDecimal("15.00"));

		// Prepare the mock for execution.
		EasyMock.replay(paymentProcessor, creditCard);

		// Test our code.
		PaymentService service = new PaymentService(paymentProcessor);
		service.charge(creditCard, new BigDecimal("15.00"));
	}

}	
