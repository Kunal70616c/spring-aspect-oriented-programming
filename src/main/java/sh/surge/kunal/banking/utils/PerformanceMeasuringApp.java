package sh.surge.kunal.banking.utils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sh.surge.kunal.banking.configurations.AppConfig;
import sh.surge.kunal.banking.models.Customer;
import sh.surge.kunal.banking.models.FullName;
import com.github.javafaker.Faker;

public class PerformanceMeasuringApp {

	public static void main(String[] args) {
		Faker faker=new Faker();
        // create a context
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
        // get the customer bean
	   Customer customer=context.getBean(Customer.class);
       // get the full name bean
	   FullName fullName=context.getBean(FullName.class);
       // set the properties
	   fullName.setFirstName(faker.name().firstName());
	   fullName.setMiddleName(faker.name().nameWithMiddle());
	   fullName.setLastName(faker.name().lastName());
	   customer.setFullName(fullName);
	   customer.setAccountNo(faker.number().numberBetween(1000000000L, 9999999999L));		
		customer.setEmail(faker.internet().emailAddress());
		customer.setContactNo(Long.parseLong(faker.phoneNumber().subscriberNumber(10)));
		customer.setPassword(faker.internet().password(8, 16, true, true, true));
        //Close the context
		context.close();

	}

}
