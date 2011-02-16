package eg.tedyoung.springproxies.ex7;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracingAspect {
	
	@Around("execution(* eg..TracedService.*(..))")
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
		Object target = joinPoint.getTarget();
		
		Logger logger = LoggerFactory.getLogger(target.getClass());
		
		logger.info("Calling Method: " + joinPoint.getSignature());
		
		Object result = joinPoint.proceed();
		
		logger.info("Exiting Method: " + joinPoint.getSignature());
		
		return result;
	}
}
