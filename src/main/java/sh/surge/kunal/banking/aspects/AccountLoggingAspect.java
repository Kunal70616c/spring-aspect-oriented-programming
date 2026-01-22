package sh.surge.kunal.banking.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect // @aspect is a marker annotation, it is used to define an aspect
@Component // @component is also needed with @aspect to make it a bean
public class AccountLoggingAspect {
    // Logger for logging
	Logger logger = LoggerFactory.getLogger(AccountLoggingAspect.class);

    // Before advice
    // advice defines what to do and when to do it
    // this advice will be executed before the target method is executed
	@Before("execution(* sh.surge.kunal.banking.models.*.*(..))")
	public void logBeforeAccountMethods(JoinPoint joinPoint) throws Throwable {
        //JoinPoint is the pointcut
        // a pointcut is a method signature that matches the target method
        // this pointcut matches all methods in the Account class
		logger.info("AccountLoggingAspect: Before logAccountMethod");
        //getSignature() returns the method signature
        //getName() returns the method name
		String methodName = joinPoint.getSignature().getName();
		logger.info("Entering method: " + methodName);
		
	}
    //Around advice
    //this advice will be executed before and after the target method is executed
    //this advice can also modify the arguments of the target method
	@Around("execution(* sh.surge.kunal.banking.models.*.*(..))")
	public Object logAroundAccountMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		//ProceedingJoinPoint is a special type of JoinPoint that allows the advice to modify the arguments of the target method

        logger.info("AccountLoggingAspect: logAccountMethod");
        //getSignature() returns the method signature
        //getName() returns the method name
		String methodName = joinPoint.getSignature().getName();
		logger.info("Entering method: " + methodName);
        //proceed() is used to execute the target method
        //proceed() can also be used to modify the arguments of the target method
		Object result = joinPoint.proceed();
		logger.info("Exiting method: " + methodName);
		return result;
	}
	//After advice
    //this advice will be executed after the target method is executed
    //this advice will be executed even if the target method throws an exception
	@After("execution(* sh.surge.kunal.banking.models.*.*(..))")
	public void logAfterAccountMethods(JoinPoint joinPoint) throws Throwable {
		logger.info("AccountLoggingAspect: After logAccountMethod");
		String methodName = joinPoint.getSignature().getName();
		logger.info("Entering method: " + methodName);
		
	}

}
