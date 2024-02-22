package com.example.community.user;

import com.example.community.institution.Institution;

public class UserBuilder {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(),user.getName(), user.getPassword(), user.getEmail(), user.getRole());
    }


    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getUsername(),
                userDTO.getName(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRole()
        );
    }

    public static User toEntity(UserDTO userDTO, Institution institution) {
        return new User(userDTO.getUsername(),
                userDTO.getName(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRole(),
                institution
        );
    }
}
