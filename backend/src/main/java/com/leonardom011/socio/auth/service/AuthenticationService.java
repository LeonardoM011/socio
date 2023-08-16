package com.leonardom011.socio.auth.service;

import com.leonardom011.socio.auth.controller.dto.AuthenticationRequest;
import com.leonardom011.socio.auth.controller.dto.AuthenticationResponse;
import com.leonardom011.socio.auth.controller.dto.RegisterRequest;
import com.leonardom011.socio.exception.CouldNotRegisterException;
import com.leonardom011.socio.exception.WrongUsernameOrPasswordException;
import com.leonardom011.socio.jwt.service.JwtService;
import com.leonardom011.socio.users.controller.dto.UserDTO;
import com.leonardom011.socio.users.entity.User;
import com.leonardom011.socio.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Clock clock;

    public AuthenticationResponse register(RegisterRequest request) throws CouldNotRegisterException {
        User user = request.toUser(passwordEncoder, LocalDateTime.now(clock));
        try {
            userRepository.save(user);
        } catch(DataAccessException e) {
            throw new CouldNotRegisterException(e.getMessage());
        }

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, new UserDTO(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws WrongUsernameOrPasswordException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()));
        } catch(AuthenticationException e) {
            throw new WrongUsernameOrPasswordException();
        }

        User user = userRepository.findByUsername(request.username()).orElseThrow();
        user.setLastLogin(LocalDateTime.now(clock));
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, new UserDTO(user));
    }

}
