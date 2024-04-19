package io.ussopm.AuthService.service;

import io.ussopm.AuthService.model.Customer;
import io.ussopm.AuthService.repository.CustomerRepository;
import io.ussopm.AuthService.security.CustomerDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findCustomerByUsername(username);

        return customer.map(CustomerDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Customer with username " + username + " was not found"));
    }
}
