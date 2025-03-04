package co.kh.app.service;

import co.kh.app.dto.AuthDto;
import co.kh.app.dto.LoginDto;
import co.kh.app.dto.RefreshTokenDto;

public interface AuthService {

    AuthDto login(LoginDto loginDto);
    AuthDto refresh(RefreshTokenDto refreshTokenDto);
}
