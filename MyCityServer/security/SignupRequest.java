package com.example.community.security;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;


@Data
@Builder
public class SignupRequest {
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;

}
