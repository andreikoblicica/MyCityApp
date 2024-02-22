package com.example.community.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO{
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String role;
}