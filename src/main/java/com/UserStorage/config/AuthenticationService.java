package com.UserStorage.config;

import com.UserStorage.models.Role;
import com.UserStorage.models.SecurityUser;
import com.UserStorage.repositories.SecurityUserRepository;
import com.UserStorage.requests.AuthenticationRequest;
import com.UserStorage.requests.RegisterRequest;
import com.UserStorage.responses.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationService(SecurityUserRepository securityUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.securityUserRepository = securityUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserName(request.getUserName());
        securityUser.setPassword(passwordEncoder.encode(request.getPassword()));
        securityUser.setRole(Role.USER);
        securityUserRepository.save(securityUser);

        String jwtToken = jwtService.generateToken(securityUser);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        SecurityUser securityUser = securityUserRepository.findByUserName(request.getUserName()).orElseThrow();
        String jwtToken = jwtService.generateToken(securityUser);
        return new AuthenticationResponse(jwtToken);
    }
}
