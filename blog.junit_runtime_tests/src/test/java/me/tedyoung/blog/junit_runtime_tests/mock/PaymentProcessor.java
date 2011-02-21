package me.tedyoung.blog.junit_runtime_tests.mock;

import java.math.BigDecimal;

public interface PaymentProcessor {
	void charge(CreditCard creditCard, BigDecimal amount);
}
