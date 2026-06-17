package com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    private String name;
    private String email;
    private String password;
}
