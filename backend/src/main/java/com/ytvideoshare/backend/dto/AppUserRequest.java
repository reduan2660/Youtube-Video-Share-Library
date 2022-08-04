package com.ytvideoshare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppUserRequest {

    @NotNull(message = "You must enter name.")
    private String name;

    @Email @NotNull(message = "You must enter a valid email.")
    private String email;

    @NotNull(message = "You must enter a password.")
    private String password;
}
