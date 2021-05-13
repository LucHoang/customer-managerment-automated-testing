package cg.wbd.grandemonstration.service;


import cg.wbd.grandemonstration.repository.CustomerRepository;
import cg.wbd.grandemonstration.service.impl.CustomerServiceImplWithSpringData;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerServiceTestConfig {
    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImplWithSpringData();
    }
    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }
}