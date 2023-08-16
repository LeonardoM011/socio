package com.leonardom011.socio.users.service;

import com.leonardom011.socio.jwt.service.JwtService;
import com.leonardom011.socio.exception.UserNotFoundException;
import com.leonardom011.socio.users.controller.dto.UserDTO;
import com.leonardom011.socio.users.controller.dto.UserResponse;
import com.leonardom011.socio.users.entity.User;
import com.leonardom011.socio.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserResponse getCurrentUserDetails() throws UsernameNotFoundException {
        System.out.println(jwtService.getCurrentUser().getAuthorities());
        return new UserResponse(jwtService.getCurrentUser());
    }

    public UserDTO getUserById(UUID userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return new UserDTO(user);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::new);
    }
}
