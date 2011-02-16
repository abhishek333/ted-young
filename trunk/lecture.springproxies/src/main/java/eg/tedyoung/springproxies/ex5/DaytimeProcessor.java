package eg.tedyoung.springproxies.ex5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaytimeProcessor implements Processor {
	Logger log = LoggerFactory.getLogger(getClass());
	
	public void process(String data) {
		log.info("Daytime processing: {}", data);
	}
}
