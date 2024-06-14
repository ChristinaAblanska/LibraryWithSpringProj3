package com.academy.LibraryProjectSpring_2.model;

import com.academy.LibraryProjectSpring_2.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class User {
    private long id;
    @NotBlank(message = "Must not be empty!")
    @Length(min = 2, max = 50, message = "Invalid length!")
    private String firstName;
    @NotBlank(message = "Must not be empty!")
    @Length(min = 2, max = 50, message = "Invalid length!")
    private String lastName;
    @Email
    private String email;
    private String phoneNumber;
    @NotNull
    private boolean isLocked;
    @NotNull
    @Pattern(regexp = "^READER$|^AUTHOR$|^ADMIN", message = "Allowed input: READER, AUTHOR or ADMIN")
    private Role role;
    @NotBlank(message = "Must not be empty!")
    @Length(min = 2, max = 50, message = "Invalid length!")
    private String userName;
    @NotBlank(message = "Must not be empty!")
    @Length(min = 2, max = 50, message = "Invalid length!")
    private String password;
}
