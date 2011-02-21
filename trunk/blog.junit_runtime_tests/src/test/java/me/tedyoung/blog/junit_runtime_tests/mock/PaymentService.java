package me.tedyoung.blog.junit_runtime_tests.mock;

import java.math.BigDecimal;

public class PaymentService {
	private PaymentProcessor paymentProcessor;

	public PaymentService(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}

	public PaymentProcessor getPaymentProcessor() {
		return paymentProcessor;
	}

	public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}

	public void charge(CreditCard creditCard, BigDecimal amount) {
		 paymentProcessor.charge(creditCard, amount);
	}
}
