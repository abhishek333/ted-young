package eg.tedyoung.springproxies.ex2;

public interface PaymentProcessor {
	
	void processPayment(String creditCardNumber, int amount) throws PaymentException;
	
}
