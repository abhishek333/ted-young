package eg.tedyoung.springproxies.ex1;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Test;

import eg.tedyoung.springproxies.Example;

public class _1 extends Example {
	@Test
	public void proxyExample() throws Exception {
		// Instantiate the invocation handler:
		EventLogger eventLogger = new EventLogger();
		
		// Create a runtime generated proxy class:
		Class<?> proxyClass = Proxy.getProxyClass(getClass().getClassLoader(), ActionListener.class, MouseListener.class);
		
		// Instantiate the proxy class:
		Object proxy = proxyClass.getConstructor(new Class<?>[]{ InvocationHandler.class }).newInstance(eventLogger);
		
		// Create the frame and set event handlers.
		TestFrame frame = new TestFrame();
		frame.button.addActionListener(((ActionListener) proxy));
		frame.panel.addMouseListener(((MouseListener) proxy));
		
		// Hack: keep system alive so we can play with the frame.
		synchronized (this) { wait(); }
	}
}
