package io.ussopm.AuthService.controller;

import io.ussopm.AuthService.dto.CustomerDTO;
import io.ussopm.AuthService.model.Customer;
import io.ussopm.AuthService.model.auth.AuthResponse;
import io.ussopm.AuthService.model.auth.LoginRequest;
import io.ussopm.AuthService.security.JwtIssuer;
import io.ussopm.AuthService.service.AuthService;
import io.ussopm.AuthService.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/api")
public class AuthRestController {


    private final AuthService authService;
    @GetMapping("/hello")
    public String greeting() {
        return "HELLO";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            return this.authService.attemptLogin(request.getUsername(), request.getPassword());
        }
    }

    @PostMapping("/registration")
    public AuthResponse registration(@RequestBody @Valid CustomerDTO customerDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            return this.authService.registration(customerDTO);
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                        ex.getMessage()));
    }
}
