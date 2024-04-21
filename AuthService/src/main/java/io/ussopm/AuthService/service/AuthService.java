package io.ussopm.AuthService.service;

import io.ussopm.AuthService.dto.CustomerDTO;
import io.ussopm.AuthService.model.Customer;
import io.ussopm.AuthService.model.auth.AuthResponse;
import io.ussopm.AuthService.security.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtIssuer issuer;
    private final CustomerService customerService;
    private final ModelMapper mapper;
    public AuthResponse attemptLogin(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = issuer.issue(username);

            return AuthResponse.builder()
                    .accessToken(token)
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public AuthResponse registration(CustomerDTO customerDTO) {
        this.customerService.save(convertToModel(customerDTO));
        String token = issuer.issue(customerDTO.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .build();
    }
    private Customer convertToModel(CustomerDTO customerDTO) {
        return this.mapper.map(customerDTO, Customer.class);
    }
}
