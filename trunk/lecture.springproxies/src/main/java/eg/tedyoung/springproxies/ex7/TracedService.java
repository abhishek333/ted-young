package eg.tedyoung.springproxies.ex7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TracedService {
	Logger log = LoggerFactory.getLogger(getClass());
	
	public void methodOne() {
		log.info("Method One");
	}
	
	public void methodTwo() {
		log.info("Method Two");
	}
}
