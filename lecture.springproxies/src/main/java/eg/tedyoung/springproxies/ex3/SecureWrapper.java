package eg.tedyoung.springproxies.ex3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SecureWrapper implements InvocationHandler {
	User user;
	Object target;
	
	public SecureWrapper(User user, Object target) {
		this.user = user;
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (user.isAuthenticated())
			return method.invoke(target, args);
		else
			return null;
	}
}
