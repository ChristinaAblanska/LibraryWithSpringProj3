package com.academy.LibraryProjectSpring_2.dto;

import com.academy.LibraryProjectSpring_2.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ResponseUserDTO(@Length(min = 2, max = 50, message = "Invalid length!") String firstName, @Length(min = 2, max = 50, message = "Invalid length!") String lastName, @Email String email, String phoneNumber, Role role, Boolean isLocked) {
}
