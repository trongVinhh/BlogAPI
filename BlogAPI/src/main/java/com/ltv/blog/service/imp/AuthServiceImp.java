package com.ltv.blog.service.imp;

import com.ltv.blog.entity.Role;
import com.ltv.blog.entity.User;
import com.ltv.blog.exception.BlogAPIException;
import com.ltv.blog.exception.ResourceNotFoundException;
import com.ltv.blog.payload.LoginDto;
import com.ltv.blog.payload.RegisterDto;
import com.ltv.blog.repository.RoleRepository;
import com.ltv.blog.repository.UserRepository;
import com.ltv.blog.security.JwtTokenProvider;
import com.ltv.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImp implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    private AuthServiceImp(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                                                                    loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        // check for username exist in db
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exist");
        }

        // check for email exist in db
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exist");
        }

        User user = this.mapToEntity(registerDto);

        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findByName("ROLE_USER");

        try {
            if (role.isPresent()) {
                roles.add(role.get());
                user.setRoles(roles);
            }
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Can not find role: ROLE_USER ");
        }

        userRepository.save(user);

        return "Register successfully";
    }

    private User mapToEntity(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        return user;
    }

}
