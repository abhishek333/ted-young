package eg.tedyoung.springproxies.ex4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

@Service
public class ExpensiveServiceImpl implements ExpensiveService {
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	@Cacheable(cacheName="cache")
	public String expensiveLookup() {
		log.info("Performing expensive lookup.");
		return "Hello World!";
	}
	
}
