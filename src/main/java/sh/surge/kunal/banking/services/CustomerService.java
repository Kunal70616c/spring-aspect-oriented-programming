package sh.surge.kunal.banking.services;

import org.springframework.stereotype.Service;
import sh.surge.kunal.banking.aspects.CustomerValidationAspect;
import sh.surge.kunal.banking.models.Customer;

@Service
public class CustomerService {


    public void addCustomer(Customer customer) {
        // Logic to add customer to the database
        // this method will be called from the before advice
        CustomerValidationAspect.validateCustomer(customer);
        // this method will be called from the after advice

        System.out.println("Customer added: " + customer);
    }

}
