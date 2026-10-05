package com.example.buildMart.controllers;

import com.example.buildMart.dtos.requests.UserAuthRequest;
import com.example.buildMart.dtos.responses.JwtTokenResponse;
import com.example.buildMart.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserAuthRequest userAuthRequest){
        userService.registerUser(userAuthRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> loginUser(@RequestBody UserAuthRequest userAuthRequest){
        return new ResponseEntity<>(userService.loginUser(userAuthRequest), HttpStatus.CREATED);
    }
}
