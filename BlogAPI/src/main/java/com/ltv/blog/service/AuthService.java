package com.ltv.blog.service;

import com.ltv.blog.payload.LoginDto;
import com.ltv.blog.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
