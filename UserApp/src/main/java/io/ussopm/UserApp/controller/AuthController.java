package io.ussopm.UserApp.controller;

import io.ussopm.UserApp.client.AuthRestClient;
import io.ussopm.UserApp.client.BadRequestException;
import io.ussopm.UserApp.controller.payload.LoginRequest;
import io.ussopm.UserApp.dto.CustomerDTO;
import io.ussopm.UserApp.model.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthRestClient restClient;
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("login")LoginRequest request) {
        return "auth/login";
    }

    @PostMapping("/login")
    public AuthResponse login(@ModelAttribute("login") LoginRequest request) {
            return this.restClient.login(request.username(), request.password());
//        return "redirect:/todo/tasks";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "/auth/logout";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("customer") CustomerDTO customerDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("customer")CustomerDTO customerDTO, Model model, HttpServletResponse response) {
        try {
            this.restClient.registration(customerDTO);
            return "redirect:/auth/login";
        } catch (BadRequestException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", ex.getErrors());
            model.addAttribute("request", customerDTO);
            return "/auth/registration";
        }
    }
}
