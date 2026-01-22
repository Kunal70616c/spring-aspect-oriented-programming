package sh.surge.kunal.banking.utils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sh.surge.kunal.banking.configurations.AppConfig;
import sh.surge.kunal.banking.models.Customer;
import sh.surge.kunal.banking.services.CustomerService;
import com.github.javafaker.Faker;

public class CustomerApp {

	public static void main(String[] args) {
		// faker is used to generate random data
        Faker faker = new Faker();
        // create a context
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        // get the customer service bean
		CustomerService customerService = context.getBean(CustomerService.class);
		// get the customer bean
        Customer customer = context.getBean(Customer.class);
        // set the properties
		customer.setAccountNo(faker.number().numberBetween(1000000000L, 9999999999L));
		customer.getFullName().setFirstName(faker.name().firstName());
		customer.getFullName().setLastName(faker.name().lastName());
		customer.setEmail(faker.internet().emailAddress());
		customer.setContactNo(Long.parseLong(faker.phoneNumber().subscriberNumber(10)));
		customer.setPassword(faker.internet().password(8, 16, true, true, true));
		//calls validation aspect before adding customer
		customerService.addCustomer(customer);
        context.close();
		
	}

}
