package cg.wbd.grandemonstration.validator;

import cg.wbd.grandemonstration.model.Customer;
import cg.wbd.grandemonstration.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Customer> {
    @Autowired
    private CustomerService customerService;

    @Override
    public boolean isValid(Customer value, ConstraintValidatorContext context) {
        return customerService
                .findAll()
                .stream()
                .filter(c -> isSameEmail(value, c))
                .allMatch(c -> isSameId(value, c));
    }

    private boolean isSameEmail(Customer c1, Customer c2) {
        return Objects.equals(c1.getEmail(), c2.getEmail());
    }

    private boolean isSameId(Customer c1, Customer c2) {
        return Objects.equals(c1.getId(), c2.getId());
    }
}
