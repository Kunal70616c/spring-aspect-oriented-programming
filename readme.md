# Spring Aspect-Oriented Programming (AOP)

A comprehensive guide to understanding and implementing Aspect-Oriented Programming in Spring Framework using a Banking Application example.

## Repository
[GitHub - Spring AOP Banking Application](https://github.com/Kunal70616c/spring-aspect-oriented-programming.git)

## Table of Contents
- [What is AOP?](#what-is-aop)
- [Why Use AOP?](#why-use-aop)
- [AOP Core Concepts](#aop-core-concepts)
- [AOP Terminology](#aop-terminology)
- [Project Structure](#project-structure)
- [Implementation Details](#implementation-details)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)

---

## What is AOP?

**Aspect-Oriented Programming (AOP)** is a programming paradigm that aims to increase modularity by allowing the separation of cross-cutting concerns. It provides a way to add additional behavior to existing code without modifying the code itself.

In traditional Object-Oriented Programming (OOP), we organize code into classes and objects. However, certain functionalities like logging, security, transaction management, and performance monitoring tend to be scattered across multiple classes, making the code harder to maintain. AOP solves this problem by extracting these cross-cutting concerns into separate modules called **Aspects**.

### Key Principle
AOP enables you to modularize concerns that cut across multiple classes and layers of your application, keeping your business logic clean and focused on its primary responsibility.

---

## Why Use AOP?

AOP offers several compelling benefits for enterprise application development:

### 1. **Separation of Concerns**
Business logic remains clean and focused on core functionality, while cross-cutting concerns like logging, validation, and monitoring are handled separately.

### 2. **Code Reusability**
Write cross-cutting logic once in an aspect and apply it to multiple classes and methods without code duplication.

### 3. **Improved Maintainability**
When you need to modify logging or security logic, you only need to change it in one place (the aspect) rather than across multiple classes.

### 4. **Reduced Code Clutter**
Your business classes don't need to be polluted with logging statements, validation checks, or performance monitoring code.

### 5. **Centralized Control**
All cross-cutting concerns are managed in a centralized location, making it easier to enable, disable, or modify them.

### Common Use Cases
- **Logging**: Automatically log method entry, exit, and exceptions
- **Security**: Enforce authentication and authorization
- **Transaction Management**: Handle database transactions declaratively
- **Performance Monitoring**: Measure and log execution time
- **Error Handling**: Centralized exception handling
- **Caching**: Implement caching strategies
- **Validation**: Input validation before method execution

---

## AOP Core Concepts

### How AOP Works

AOP works by creating **proxy objects** at runtime. When you call a method on an object that has aspects applied to it, you're actually calling the proxy object, which executes the aspect logic before/after/around the actual method.

```
Client → Proxy Object → Aspect Logic → Target Object → Business Method
```

### Types of AOP

1. **Compile-time weaving**: Aspects are woven during compilation (AspectJ)
2. **Load-time weaving**: Aspects are woven when classes are loaded into JVM
3. **Runtime weaving**: Spring AOP uses runtime proxy-based weaving

Spring AOP uses **runtime weaving** through JDK dynamic proxies or CGLIB proxies, making it easier to use but with some limitations compared to full AspectJ.

---

## AOP Terminology

Understanding AOP requires familiarity with its specific terminology:

### 1. **Aspect**
A module that encapsulates a cross-cutting concern. In Spring, an aspect is a class annotated with `@Aspect`.

```java
@Aspect
@Component
public class AccountLoggingAspect {
    // Aspect implementation
}
```

### 2. **Join Point**
A point during the execution of a program, such as the execution of a method or the handling of an exception. In Spring AOP, a join point always represents a method execution.

```java
public void logBeforeAccountMethods(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    // joinPoint represents the method being intercepted
}
```

### 3. **Advice**
Action taken by an aspect at a particular join point. This is the actual code that runs when a join point is reached.

**Types of Advice:**

- **@Before**: Executes before the target method
- **@After**: Executes after the target method (regardless of outcome)
- **@AfterReturning**: Executes after the target method returns successfully
- **@AfterThrowing**: Executes if the target method throws an exception
- **@Around**: Executes before and after the target method, with full control over execution

### 4. **Pointcut**
A predicate that matches join points. Pointcuts determine where advice should be applied.

```java
@Before("execution(* sh.surge.kunal.banking.models.*.*(..))")
//       ↑ This is the pointcut expression
```

### 5. **Target Object**
The object being advised by one or more aspects. Also called the "advised object."

### 6. **AOP Proxy**
An object created by the AOP framework to implement the aspect contracts. In Spring AOP, this is either a JDK dynamic proxy or a CGLIB proxy.

### 7. **Weaving**
The process of linking aspects with other application types or objects to create an advised object. This can be done at compile time, load time, or runtime.

---

## Pointcut Expressions

Pointcut expressions define where aspects should be applied. They follow this pattern:

```
execution(modifiers? return-type declaring-type? method-name(params) throws?)
```

### Common Patterns

```java
// Match all methods in a specific package
execution(* sh.surge.kunal.banking.models.*.*(..))

// Match all methods in CustomerService
execution(* sh.surge.kunal.banking.services.CustomerService.*(..))

// Match specific method with specific parameters
execution(* sh.surge.kunal.banking.services.CustomerService.addCustomer(..))

// Match methods that start with "get"
execution(* sh.surge.kunal.banking.models.*.get*(..))
```

### Wildcard Meanings
- `*`: Matches any number of characters
- `..`: Matches any number of parameters or packages
- `+`: Matches subclasses

---

## Project Structure

```
sh.surge.kunal.banking/
├── aspects/
│   ├── AccountLoggingAspect.java       # Logging cross-cutting concern
│   ├── CustomerValidationAspect.java   # Validation cross-cutting concern
│   └── PerformanceAspect.java          # Performance monitoring
├── configurations/
│   └── AppConfig.java                   # Spring configuration with AOP enabled
├── models/
│   ├── Account.java                     # Abstract account entity
│   ├── CurrentAccount.java              # Current account implementation
│   ├── SavingsAccount.java              # Savings account implementation
│   ├── Customer.java                    # Customer entity
│   └── FullName.java                    # Name value object
├── services/
│   └── CustomerService.java             # Business logic layer
└── utils/
    ├── AccountApp.java                  # Demo: Account with logging
    ├── CustomerApp.java                 # Demo: Customer with validation
    └── PerformanceMeasuringApp.java     # Demo: Performance monitoring
```

---

## Implementation Details

### 1. Enabling AOP in Spring

First, enable AspectJ auto-proxy in your configuration:

```java
@Configuration
@ComponentScan(basePackages = "sh.surge.kunal.banking")
@EnableAspectJAutoProxy
public class AppConfig {
    // @EnableAspectJAutoProxy creates proxy objects for aspect weaving
}
```

The `@EnableAspectJAutoProxy` annotation tells Spring to:
- Scan for classes annotated with `@Aspect`
- Create proxy objects for beans that match pointcut expressions
- Weave aspect logic into method executions

---

### 2. Account Logging Aspect

This aspect demonstrates all three main advice types: `@Before`, `@Around`, and `@After`.

```java
@Aspect
@Component
public class AccountLoggingAspect {
    Logger logger = LoggerFactory.getLogger(AccountLoggingAspect.class);

    // BEFORE ADVICE
    @Before("execution(* sh.surge.kunal.banking.models.*.*(..))")
    public void logBeforeAccountMethods(JoinPoint joinPoint) {
        logger.info("AccountLoggingAspect: Before advice");
        String methodName = joinPoint.getSignature().getName();
        logger.info("Entering method: " + methodName);
    }

    // AROUND ADVICE
    @Around("execution(* sh.surge.kunal.banking.models.*.*(..))")
    public Object logAroundAccountMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("AccountLoggingAspect: Around advice - Before execution");
        String methodName = joinPoint.getSignature().getName();
        logger.info("Entering method: " + methodName);
        
        // Execute the actual method
        Object result = joinPoint.proceed();
        
        logger.info("Exiting method: " + methodName);
        return result;
    }

    // AFTER ADVICE
    @After("execution(* sh.surge.kunal.banking.models.*.*(..))")
    public void logAfterAccountMethods(JoinPoint joinPoint) {
        logger.info("AccountLoggingAspect: After advice");
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method executed: " + methodName);
    }
}
```

**Key Points:**
- `JoinPoint` provides access to method signature and arguments
- `ProceedingJoinPoint` (for @Around) allows you to control method execution
- `joinPoint.proceed()` executes the target method
- All getters/setters in model classes are intercepted by this aspect

**Execution Flow:**
```
1. @Before advice executes
2. @Around advice (before proceed())
3. Target method executes
4. @Around advice (after proceed())
5. @After advice executes
```

---

### 3. Customer Validation Aspect

This aspect demonstrates validation before method execution and exception handling.

```java
@Aspect
@Component
@Slf4j
public class CustomerValidationAspect {
    Logger logger = LoggerFactory.getLogger(CustomerValidationAspect.class);

    // Validation patterns
    static final Pattern namePattern = Pattern.compile("^[a-zA-Z]{5,25}$");
    static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    static final Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");

    // BEFORE ADVICE with argument binding
    @Before("execution(* sh.surge.kunal.banking.services.CustomerService.addCustomer(..)) && args(customer)")
    public void addCustomerValidation(Customer customer) throws Throwable {
        logger.info("Start - addCustomerValidation");
        // Validation logic can be added here
        logger.info("End - addCustomerValidation");
    }

    // AFTER THROWING ADVICE
    @AfterThrowing(
        pointcut = "execution(* sh.surge.kunal.banking.services.CustomerService.*(..))",
        throwing = "ex"
    )
    public void addCustomerValidationException(JoinPoint joinPoint, Throwable ex) {
        logger.info("Exception in CustomerService method: Invalid customer details - " + ex.getMessage());
    }

    // Static validation method
    public static void validateCustomer(Customer customer) {
        if(customer.getAccountNo() < 0 || String.valueOf(customer.getAccountNo()).length() < 10) {
            throw new IllegalArgumentException("Invalid account number");
        }
        if(customer.getFullName().getFirstName() == null || 
           !namePattern.matcher(customer.getFullName().getFirstName()).matches()) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if(customer.getFullName().getLastName() == null ||
           !namePattern.matcher(customer.getFullName().getLastName()).matches()) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if(customer.getEmail() == null || 
           !emailPattern.matcher(customer.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
        if(customer.getContactNo() < 0 || 
           String.valueOf(customer.getContactNo()).length() < 10) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        if(customer.getPassword() == null || 
           !passwordPattern.matcher(customer.getPassword()).matches()) {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
```

**Key Points:**
- `args(customer)` binds method arguments to advice parameters
- `@AfterThrowing` catches exceptions and logs them
- Validation rules ensure data integrity before database operations
- Regular expressions validate name, email, and password formats

**Validation Rules:**
- Account number: minimum 10 digits
- Name: 5-25 alphabetic characters
- Email: standard email format
- Password: 8-20 characters with uppercase, lowercase, digit, and special character
- Contact number: minimum 10 digits

---

### 4. Performance Monitoring Aspect

This aspect measures and logs method execution time.

```java
@Aspect
@Component
public class PerformanceAspect {
    Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* sh.surge.kunal.banking.models.*.*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // Capture start time
        long startTime = System.nanoTime();
        
        // Execute the target method
        Object result = joinPoint.proceed();
        
        // Calculate duration
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // Convert to milliseconds
        
        logger.info("Method " + joinPoint.getSignature().getName() + 
                   " executed in " + duration + " ms");
        
        return result;
    }
}
```

**Key Points:**
- Uses `@Around` advice to measure time before and after method execution
- `System.nanoTime()` provides high-precision timing
- Useful for identifying performance bottlenecks
- Can be extended to log slow methods or trigger alerts

**Use Cases:**
- Performance profiling in development
- Production monitoring for slow operations
- SLA compliance checking
- Identifying optimization opportunities

---

### 5. Business Service Layer

The service layer contains business logic, free from cross-cutting concerns:

```java
@Service
public class CustomerService {
    public void addCustomer(Customer customer) {
        // Validation is handled by CustomerValidationAspect
        CustomerValidationAspect.validateCustomer(customer);
        
        // Business logic
        System.out.println("Customer added: " + customer);
    }
}
```

**Benefits:**
- Clean, focused business logic
- Cross-cutting concerns handled by aspects
- Easy to test in isolation
- Maintainable and readable code

---

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Clone and Run

```bash
# Clone the repository
git clone https://github.com/Kunal70616c/spring-aspect-oriented-programming.git

# Navigate to project directory
cd spring-aspect-oriented-programming

# Build the project
mvn clean install

# Run demos (choose one)
mvn exec:java -Dexec.mainClass="sh.surge.kunal.banking.utils.AccountApp"
mvn exec:java -Dexec.mainClass="sh.surge.kunal.banking.utils.CustomerApp"
mvn exec:java -Dexec.mainClass="sh.surge.kunal.banking.utils.PerformanceMeasuringApp"
```

---

## Running the Application

### 1. AccountApp - Logging Demo

Demonstrates `@Before`, `@Around`, and `@After` advice on Account model methods.

```java
public class AccountApp {
    public static void main(String[] args) {
        Faker faker = new Faker();
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        
        Account savingsAccount = (Account) context.getBean("savingsAccount");
        savingsAccount.setAccountNumber(faker.number().numberBetween(100000, 100000000));
        savingsAccount.setOpeningDate(LocalDate.now());
        savingsAccount.setRunningTotal(faker.number().numberBetween(1000, 100000));
        
        context.close();
    }
}
```

**Expected Output:**
```
AccountLoggingAspect: Before advice
Entering method: setAccountNumber
AccountLoggingAspect: Around advice - Before execution
Entering method: setAccountNumber
Exiting method: setAccountNumber
AccountLoggingAspect: After advice
Method executed: setAccountNumber
...
```

---

### 2. CustomerApp - Validation Demo

Demonstrates customer validation before adding to database.

```java
public class CustomerApp {
    public static void main(String[] args) {
        Faker faker = new Faker();
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        
        CustomerService customerService = context.getBean(CustomerService.class);
        Customer customer = context.getBean(Customer.class);
        
        // Set customer properties with valid data
        customer.setAccountNo(faker.number().numberBetween(1000000000L, 9999999999L));
        customer.getFullName().setFirstName(faker.name().firstName());
        customer.getFullName().setLastName(faker.name().lastName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setContactNo(Long.parseLong(faker.phoneNumber().subscriberNumber(10)));
        customer.setPassword(faker.internet().password(8, 16, true, true, true));
        
        // Validation aspect executes before adding customer
        customerService.addCustomer(customer);
        
        context.close();
    }
}
```

**Expected Output (Valid Data):**
```
Start - addCustomerValidation
End - addCustomerValidation
Customer added: Customer(accountNo=1234567890, fullName=...)
```

**Expected Output (Invalid Data):**
```
Start - addCustomerValidation
Exception in CustomerService method: Invalid customer details - Invalid email
```

---

### 3. PerformanceMeasuringApp - Performance Demo

Demonstrates performance monitoring for model operations.

```java
public class PerformanceMeasuringApp {
    public static void main(String[] args) {
        Faker faker = new Faker();
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        
        Customer customer = context.getBean(Customer.class);
        FullName fullName = context.getBean(FullName.class);
        
        fullName.setFirstName(faker.name().firstName());
        fullName.setMiddleName(faker.name().nameWithMiddle());
        fullName.setLastName(faker.name().lastName());
        customer.setFullName(fullName);
        customer.setAccountNo(faker.number().numberBetween(1000000000L, 9999999999L));
        
        context.close();
    }
}
```

**Expected Output:**
```
Method setFirstName executed in 2 ms
Method setMiddleName executed in 1 ms
Method setLastName executed in 1 ms
Method setFullName executed in 3 ms
Method setAccountNo executed in 1 ms
```

---

## Advice Execution Order

When multiple aspects and advice types are applied to a single method:

```
1. @Before advice (all aspects)
2. @Around advice - before proceed() (all aspects)
3. Target method execution
4. @Around advice - after proceed() (all aspects)
5. @AfterReturning or @AfterThrowing (depending on outcome)
6. @After advice (all aspects)
```

---

## Best Practices

### 1. Keep Aspects Focused
Each aspect should handle one cross-cutting concern. Don't mix logging, validation, and security in the same aspect.

### 2. Use Specific Pointcut Expressions
Avoid overly broad pointcuts that match too many methods:
```java
// Too broad - matches everything
execution(* *(..))

// Better - specific package and class
execution(* sh.surge.kunal.banking.services.*.*(..))
```

### 3. Avoid Aspect Dependencies
Aspects should be independent and not depend on each other's execution order.

### 4. Performance Considerations
- @Around advice has more overhead than @Before or @After
- Be cautious with aspects on frequently called methods
- Consider using AspectJ compile-time weaving for better performance

### 5. Exception Handling
Always handle exceptions properly in aspects, especially in @Around advice:
```java
@Around("pointcut")
public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
        return joinPoint.proceed();
    } catch (Exception e) {
        logger.error("Error in method: " + joinPoint.getSignature(), e);
        throw e;
    }
}
```

### 6. Testing
Test aspects separately from business logic using integration tests with Spring context.

---

## Common Pitfalls

### 1. Self-Invocation
AOP doesn't work when a method calls another method in the same class:
```java
public class MyService {
    public void method1() {
        method2(); // Aspect won't be applied!
    }
    
    public void method2() {
        // Business logic
    }
}
```

### 2. Private Methods
Spring AOP cannot intercept private methods (only public methods are proxied).

### 3. Final Classes/Methods
CGLIB proxies cannot proxy final classes or methods.

### 4. Incorrect Pointcut Expressions
Double-check your package names and method signatures in pointcut expressions.

---

## Advantages of This Implementation

1. **Clean Separation**: Business logic is separate from logging, validation, and monitoring
2. **Reusability**: Aspects can be applied to multiple classes without code duplication
3. **Maintainability**: Changes to cross-cutting concerns are localized to aspect classes
4. **Testability**: Business logic can be tested without aspect overhead
5. **Flexibility**: Aspects can be enabled/disabled without changing business code

---

## Conclusion

This banking application demonstrates how Spring AOP enables clean, maintainable code by separating cross-cutting concerns from business logic. The three aspects (logging, validation, and performance monitoring) showcase the power and flexibility of AOP in real-world applications.

By understanding and implementing AOP, you can build enterprise applications that are:
- More modular and organized
- Easier to maintain and extend
- Less prone to code duplication
- Better aligned with separation of concerns principles

---

## Additional Resources

- [Spring AOP Documentation](https://docs.spring.io/spring-framework/reference/core/aop.html)
- [AspectJ Documentation](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)
- [Spring Framework Reference](https://docs.spring.io/spring-framework/docs/current/reference/html/)

---

## License

This project is open-source and available for educational purposes.

## Author

Kunal - [GitHub Profile](https://github.com/Kunal70616c)
