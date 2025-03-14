package co.kh.app.service.Impl;

import co.kh.app.dto.AuthDto;
import co.kh.app.dto.RefreshTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
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
    private final JwtEncoder accessTokenJwtEncoder;
    private JwtEncoder refreshTokenJwtEncoder;
    @Autowired
    public void setRefreshTokenJwtEncoder(@Qualifier("refreshTokenJwtEncoder")
                                              JwtEncoder refreshTokenJwtEncoder){
        this.refreshTokenJwtEncoder = refreshTokenJwtEncoder;
    }

    private final DaoAuthenticationProvider provider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    @Override
    public AuthDto login(LoginDto loginDto) {
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(),
                loginDto.password());
        auth = provider.authenticate(auth);

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.contains("ROLE_"))
                .collect(Collectors.joining(" "));

        JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer("client")
                .expiresAt(now.plus(1,ChronoUnit.SECONDS))
                .claim("scope",scope)
                .build();

        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer("client")
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(auth.getName())
                .claim("scope",scope)
                .build();

        String accessToken = accessTokenJwtEncoder.encode(
                JwtEncoderParameters.from(accessTokenJwtClaimsSet)
        ).getTokenValue();

        String refreshToken = refreshTokenJwtEncoder.encode(
                JwtEncoderParameters.from(refreshTokenJwtClaimsSet)
        ).getTokenValue();

        return new AuthDto(accessToken, refreshToken);

    }
    @Override
    public AuthDto refresh(RefreshTokenDto refreshTokenDto) {
        Instant now = Instant.now();

        BearerTokenAuthenticationToken token = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());

        Authentication auth = jwtAuthenticationProvider.authenticate(token);

        Jwt jwt = (Jwt) auth.getCredentials();
        Instant expiration = jwt.getExpiresAt();
        if(expiration !=null && expiration.isBefore(now)){
            throw new RuntimeException("Refresh token has expired");
        }

        System.out.println("Scope by key: " + jwt.getClaims().get("scope"));
        System.out.println("Scope 2: " + jwt.getClaimAsString("scope"));
        System.out.println("Scope 3: " + jwt.getClaims());

        JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer("client")
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(auth.getName())
                .claim("scope", jwt.getClaimAsString("scope"))
                .build();

        String accessToken = accessTokenJwtEncoder.encode(
            JwtEncoderParameters.from(accessTokenJwtClaimsSet)
        ).getTokenValue();

        return new AuthDto(accessToken, refreshTokenDto.refreshToken());
    }

}
