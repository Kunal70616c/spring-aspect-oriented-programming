package sh.surge.kunal.banking.aspects;

import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import sh.surge.kunal.banking.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Aspect // @aspect is a marker annotation, it is used to define an aspect
@Component
@Slf4j
public class CustomerValidationAspect {

    // Logger for logging
    Logger logger = LoggerFactory.getLogger(CustomerValidationAspect.class);

    // Regular expressions for validation
    static final Pattern namePattern=Pattern.compile("^[a-zA-Z]{5,25}$");
    static final Pattern emailPattern=Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    static final Pattern passwordPattern=Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
	

    // Before advice
    // advice defines what to do and when to do it
    // this advice will be executed before the target method is executed
    // && args(customer) is used to pass the arguments to the advice
    @Before("execution(* services.sh.surge.kunal.banking.CustomerService.addCustomer(..)) && args(customer)"
	  ) 
	  public void addCustomerValidation(Customer customer ) throws Throwable {
		  logger.info("Start - addCustomerValidation"); 	  
		 		  
		  logger.info("End - addCustomerValidation"); 
	  }
	 
	
	@AfterThrowing(
            pointcut = "execution(* services.sh.surge.kunal.banking.CustomerService.*(..))",
            throwing = "ex"
    ) // @AfterThrowing is used to define an after throwing advice
    // pointcut is used to define the pointcut expression
    // throwing is used to define the exception to be thrown
	public void addCustomerValidationException(JoinPoint joinPoint, Throwable ex) {
		logger.info("Exception in CustomerService method: "
				+ "Invalid customer details"+ex.getMessage());
	}
	
	public static void validateCustomer(Customer customer) {
	    // Logic to validate customer details
	    // this method will be called from the before advice
		if(customer.getAccountNo() < 0 
				|| String.valueOf(customer.getAccountNo()).length()<10){
			
			throw new IllegalArgumentException("Invalid account number");
		}
		if(customer.getFullName().getFirstName()==null ||  
				!namePattern.matcher(customer.getFullName().getFirstName()).matches()) {
			throw new IllegalArgumentException("Invalid first name");
		}
		if(customer.getFullName().getLastName()==null ||
				!namePattern.matcher(customer.getFullName().getLastName()).matches()) {
			throw new IllegalArgumentException("Invalid last name");
		}
		
		if(customer.getEmail()==null || 
				!emailPattern.matcher(customer.getEmail()).matches()) {
			throw new IllegalArgumentException("Invalid email");
		}
		
		if(customer.getContactNo()<0 || 
				String.valueOf(customer.getContactNo()).length()<10) {
			throw new IllegalArgumentException("Invalid phone number");
		}
		if(customer.getPassword()==null || 
				!passwordPattern.matcher(customer.getPassword()).matches()) {
			throw new IllegalArgumentException("Invalid password");
		}
	}

}
