package com.example.community.user;

import com.example.community.institution.Institution;
import com.example.community.institution.InstitutionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository,InstitutionRepository institutionRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.institutionRepository=institutionRepository;
        this.encoder = encoder;
    }

    public UserDTO save(UserDTO user, String name){
        user.setPassword(encoder.encode(user.getPassword()));
        if(name!=null){
            Institution institution=institutionRepository.findByName(name).orElse(null);
            return UserBuilder.toUserDTO(userRepository.save(UserBuilder.toEntity(user,institution)));
        }
        return UserBuilder.toUserDTO(userRepository.save(UserBuilder.toEntity(user)));
    }

    public List<UserDTO> findAll(){
        return userRepository.findAll().stream().map(UserBuilder::toUserDTO).collect(Collectors.toList());
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    public UserDTO findById(Long id){
        Optional<User> user=userRepository.findById(id);
        return user.map(UserBuilder::toUserDTO).orElse(null);

    }

    public UserDTO update(UserDTO userDTO){
        User user=userRepository.findById(userDTO.getId()).orElseThrow(EntityNotFoundException::new);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if(!userDTO.getPassword().equals("")){
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        return UserBuilder.toUserDTO(userRepository.save(user));
    }
}
