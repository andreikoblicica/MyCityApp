package com.example.community.user;

import com.example.community.security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;


@RestController
@RequestMapping(USERS)
public class UserController {


    private final UserService userService;

    private final AuthService authService;

    public UserController(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService=authService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<UserDTO>> allUsers() {
        List<UserDTO> users= userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO user) {
        UserDTO userDTO= userService.update(user);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO user) {
        UserDTO userDTO=  userService.save(user,null);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(INSTITUTION_NAME)
    public ResponseEntity<UserDTO> createInstitutionUser(@Valid @RequestBody UserDTO user,@PathVariable String name) {
        UserDTO userDTO=  userService.save(user,name);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(EXISTS_USERNAME)
    public ResponseEntity<Boolean> existsUsername(@PathVariable String username) {
        System.out.println(username);
        return new ResponseEntity<>(authService.existsByUsername(username), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(EXISTS_EMAIL)
    public ResponseEntity<Boolean> existsEmail(@PathVariable String email) {
        return new ResponseEntity<>(authService.existsByEmail(email), HttpStatus.OK);
    }


}

