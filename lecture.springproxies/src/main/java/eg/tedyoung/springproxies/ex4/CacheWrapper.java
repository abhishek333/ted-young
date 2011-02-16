package eg.tedyoung.springproxies.ex4;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

// Don't do this at home.
public class CacheWrapper implements InvocationHandler {
	Object target;
	
	HashMap<String, Object> cache = new HashMap<String, Object>();
	
	public CacheWrapper(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String key = method.toString();
		
		if (cache.containsKey(key))
			return cache.get(key);
		
		Object value = method.invoke(target, args);
		cache.put(key, value);
		
		return value;
	}

}
