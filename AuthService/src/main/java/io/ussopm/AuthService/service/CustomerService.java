package io.ussopm.AuthService.service;

import io.ussopm.AuthService.model.Authority;
import io.ussopm.AuthService.model.Customer;
import io.ussopm.AuthService.repository.AuthoritiesRepository;
import io.ussopm.AuthService.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    public void save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        List<Authority> authorities = authoritiesRepository.findAuthoritiesByRole("ROLE_USER");
        customer.setAuthorities(authorities);
        this.customerRepository.save(customer);
    }
}
