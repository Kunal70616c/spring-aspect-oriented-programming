package sh.surge.kunal.banking.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.cognizant.banking")
@EnableAspectJAutoProxy // @EnableAspectJAutoProxy is used to enable aspectJ auto proxy
// what it does is it will create a proxy object for the target object
// this proxy object will be used to invoke the target method
public class AppConfig {

}
