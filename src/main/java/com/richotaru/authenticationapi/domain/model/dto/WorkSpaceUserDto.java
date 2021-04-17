package com.richotaru.authenticationapi.domain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class WorkSpaceUserDto {
    @NotBlank(message = "Client Key cannot be empty")
    private String clientKey;
    @NotBlank(message = "Workspace Code cannot be empty")
    private String workspaceCode;
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
    private String otherNames;
    @NotBlank(message = "Display Name cannot be empty")
    private String displayName;
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Phone Number cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "Email cannot be empty")
    private String email;
    private LocalDate dob;
    private String gender;

}
