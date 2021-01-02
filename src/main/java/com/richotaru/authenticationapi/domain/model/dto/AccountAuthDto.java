package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountAuthDto {
    private String username;
    private String password;
    private AccountTypeConstant accountTypeConstant;
}
