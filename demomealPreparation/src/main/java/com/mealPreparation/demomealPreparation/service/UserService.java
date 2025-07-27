package com.mealPreparation.demomealPreparation.service;

import com.mealPreparation.demomealPreparation.dto.UserDto;
import com.mealPreparation.demomealPreparation.entity.User;
import com.mealPreparation.demomealPreparation.repository.UserRepo;
import com.mealPreparation.demomealPreparation.util.JwtUtil;

import lombok.AllArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    

    public String register(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }
        
        User user = new User();
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        
        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }

    public String login(String email, String password){
        if(email==null || password==null){
            throw new RuntimeException("email and password required");
        }
        User user= userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("invalid credentials"));
        if(passwordEncoder.matches(password, user.getPassword())){
            return jwtUtil.generateToken(user);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("No authenticated user found");
        }
        
        if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        
        throw new RuntimeException("Invalid authentication principal");
    }
}
