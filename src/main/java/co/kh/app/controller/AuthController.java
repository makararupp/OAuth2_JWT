package co.kh.app.controller;

import co.kh.app.dto.AuthDto;
import co.kh.app.dto.RefreshTokenDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kh.app.dto.LoginDto;
import co.kh.app.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthDto login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    @PostMapping("/refresh")
    public AuthDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
       return authService.refresh(refreshTokenDto);
    }
    
}
