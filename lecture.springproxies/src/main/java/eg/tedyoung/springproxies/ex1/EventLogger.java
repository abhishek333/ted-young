package eg.tedyoung.springproxies.ex1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogger implements InvocationHandler {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.info(args[0].toString());
		return null;
	}
}
