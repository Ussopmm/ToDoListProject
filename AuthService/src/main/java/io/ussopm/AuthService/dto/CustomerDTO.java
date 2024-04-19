package io.ussopm.AuthService.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50")
    private String username;

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 2, max = 100, message = "Password should be between 3 and 50")
    private String password;

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 10, max = 100, message = "Phone number must contain at least 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Email(message = "field have to be in email format *name@email.com*")
    private String email;
}
