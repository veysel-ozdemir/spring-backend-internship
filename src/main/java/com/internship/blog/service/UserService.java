package com.internship.blog.service;

import com.internship.blog.dto.UserDto;
import com.internship.blog.dto.UserEditDto;
import com.internship.blog.dto.UserLoginDto;
import com.internship.blog.model.User;
import com.internship.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseEntity<Object> login(UserLoginDto userLoginDto) {
        // get the present user
        User loginUser = userRepository.findByEmailAndPassword(
                userLoginDto.email(),
                userLoginDto.password()
        ).orElse(null);
        // check whether the credentials match
        if (loginUser != null) {
            // return the mapped entity
            return ResponseEntity.ok(userMapper.toUserResponseDto(loginUser));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    public ResponseEntity<Object> register(UserDto userDto) {
        // check whether the entered email is available
        if (userRepository.findByEmail(userDto.email()).isEmpty()) {
            // map the entity to User
            User user = userMapper.toUser(userDto);
            // save the entity
            userRepository.save(user);
            // return the mapped entity
            return ResponseEntity.ok(userMapper.toUserResponseDto(user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The entered email is already in use");
    }

    public ResponseEntity<Object> updateUser(UserEditDto userEditDto) {
        // get present user
        User presentUser = userRepository.findById(userEditDto.userId()).orElse(null);
        // create an assertion whether the user exists
        assert presentUser != null : "User not found with provided id" + userEditDto.userId();
        // check whether the entered email is available or changed
        // true  || false  : available email
        // false || true   : present email (not updated)
        // false || false  : email already in use
        // true  || true   : impossible case
        if (userRepository.findByEmail(userEditDto.email()).isEmpty() || presentUser.getEmail().equals(userEditDto.email())) {
            // map the entity
            User updatedUser = userMapper.toUpdatedUser(userEditDto, presentUser);
            // save the changes
            userRepository.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The entered email is already in use");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public String deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "User not found with id: " + id;
        }
        userRepository.deleteById(id);
        return "User with id " + id + " was deleted";
    }

}