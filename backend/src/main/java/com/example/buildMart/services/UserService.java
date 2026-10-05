package com.example.buildMart.services;

import com.example.buildMart.dtos.requests.UserAuthRequest;
import com.example.buildMart.dtos.responses.JwtTokenResponse;
import com.example.buildMart.mappers.interfaces.UserMapper;
import com.example.buildMart.models.User;
import com.example.buildMart.models.enums.Role;
import com.example.buildMart.repositories.UserRepository;
import com.example.buildMart.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    public void registerUser(UserAuthRequest userAuthRequest){
        userRepository.findByEmail(userAuthRequest.getEmail()).ifPresent(u -> {throw new RuntimeException("User already exists");});
        User user = userMapper.toEntity(userAuthRequest);
        user.setPasswordHash(passwordEncoder.encode(userAuthRequest.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
    }

    public JwtTokenResponse loginUser(UserAuthRequest userAuthRequest){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthRequest.getEmail(), userAuthRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        return new JwtTokenResponse(jwt);
    }
}
