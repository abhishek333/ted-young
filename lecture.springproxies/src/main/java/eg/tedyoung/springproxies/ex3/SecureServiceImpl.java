package eg.tedyoung.springproxies.ex3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SecureServiceImpl implements SecureService {
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void secureMethod() {
		log.info("Secure method executed.");
	}
}
