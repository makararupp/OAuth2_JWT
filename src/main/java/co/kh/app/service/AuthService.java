package co.kh.app.service;

import co.kh.app.dto.LoginDto;

public interface AuthService {

    String login(LoginDto loginDto);
}
