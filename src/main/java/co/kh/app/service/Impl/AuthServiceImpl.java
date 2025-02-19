package co.kh.app.service.Impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import co.kh.app.dto.LoginDto;
import co.kh.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final JwtEncoder jwtEncoder;
    private final DaoAuthenticationProvider provider;
    @Override
    public String login(LoginDto loginDto) {
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(),
                loginDto.password());
        auth = provider.authenticate(auth);

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.contains("ROLE_"))
                .collect(Collectors.joining(" "));

        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer("client")
                .expiresAt(now.plus(1, ChronoUnit.SECONDS))
                .subject(auth.getName())
                .claim("scope",scope)
                .build();

        String accessToken = jwtEncoder.encode(
                JwtEncoderParameters.from(refreshTokenJwtClaimsSet)
        ).getTokenValue();
        
        return accessToken;

    }
    
}
