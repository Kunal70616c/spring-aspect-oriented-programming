package sh.surge.kunal.banking.utils;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sh.surge.kunal.banking.configurations.AppConfig;
import sh.surge.kunal.banking.models.Account;

public class AccountApp {

    public static void main(String[] args) {
        Faker faker = new Faker();
        // create a context
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        // get the bean
        Account savingsAccount =
                (Account) context.getBean("savingsAccount");
        // set the properties
        savingsAccount.setAccountNumber(faker.number().numberBetween(100000, 100000000));
        savingsAccount.setOpeningDate(faker.date().birthday().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate());
        savingsAccount.setRunningTotal(faker.number().numberBetween(1000, 100000));
        //Close the context
        context.close();

    }

}
