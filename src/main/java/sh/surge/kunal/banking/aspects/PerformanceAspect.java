package sh.surge.kunal.banking.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
	// Performance monitoring advice can be added here
	Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);
	@Around("execution(* sh.surge.kunal.banking.models.*.*(..))")
    // @Around is used to define an around advice
    // pointcut is used to define the pointcut expression
	public void monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // ProceedingJoinPoint is a special type of JoinPoint that allows the advice to modify the arguments of the target method
        // joinPoint.proceed() is used to execute the target method
		// Implementation for performance monitoring
		long startTime=System.nanoTime(); // start time in nanoseconds
		Object result=joinPoint.proceed(); // execute the target method
		long endTime=System.nanoTime(); // end time in nanoseconds
		long duration=endTime-startTime/1000000; // duration in milliseconds
		logger.info("Method "+joinPoint.getSignature().getName()+" executed in "+duration+" ms");
	}
}
