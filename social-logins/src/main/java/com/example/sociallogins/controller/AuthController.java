package com.example.sociallogins.controller;

import com.example.sociallogins.repository.UserRepository;
import com.example.sociallogins.exception.BadRequestException;
import com.example.sociallogins.model.*;
import com.example.sociallogins.payload.ApiResponse;
import com.example.sociallogins.payload.AuthResponse;
import com.example.sociallogins.payload.LoginRequest;
import com.example.sociallogins.payload.SignUpRequest;
import com.example.sociallogins.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity<Object> handleLogin(@RequestBody LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenProvider.createToken(authenticate);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("signup")
    public ResponseEntity<?> handleRegister(@RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        System.out.println(location);

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

}
