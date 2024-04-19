package io.ussopm.UserApp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "t_customer")
@Data
@NoArgsConstructor
public class Customer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_name")
    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50")
    private String username;

    @Column(name = "c_password")
    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 2, max = 100, message = "Password should be between 3 and 50")
    private String password;

    @Column(name = "c_phone_number")
    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 10, max = 100, message = "Phone number must contain at least 10 digits")
    private String phoneNumber;

    @Column(name = "c_email")
    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Email(message = "field have to be in email format *name@email.com*")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_customer_authorities",
            joinColumns = @JoinColumn(name = "id_customer"),
            inverseJoinColumns = @JoinColumn(name = "id_authorities"))
    private List<Authority> authorities;

    public Customer(String username, String password, String phoneNumber, String email, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.authorities = authorities;
    }

}
