package eg.tedyoung.springproxies.ex5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NighttimeProcessor implements Processor {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void process(String data) {
		log.info("Nighttime processing: {}", data);
	}
}
