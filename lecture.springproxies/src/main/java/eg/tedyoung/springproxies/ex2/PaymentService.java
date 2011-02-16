package eg.tedyoung.springproxies.ex2;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	PaymentProcessor paymentProcessor;
	
	public boolean processPayment(String creditCardNumber, int amount) {
		try {
			paymentProcessor.processPayment(creditCardNumber, amount);
			return true;
		} catch (PaymentException e) {
			return false;
		}
	}

	void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}
}
