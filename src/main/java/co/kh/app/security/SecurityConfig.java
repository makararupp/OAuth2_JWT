package co.kh.app.security;
import co.kh.app.util.KeyUtil;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   // private final RSAKey  rsaKey;
    private final KeyUtil keyUtil;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Bean 
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public JwtAuthenticationConverter   jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    @Qualifier("jwtRefreshTokenAuthProvider")
    public JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(refreshTokenJwtDecoder());
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(token -> token.disable());
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/anonymous/**","/api/v1/auth/**").permitAll();
            auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();// Allow Swagger endpoints
            auth.requestMatchers(AUTH_WHITELIST).permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAuthority("SCOPE_book:read");
            auth.requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAuthority("SCOPE_book:write");
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAuthority("SCOPE_book:update");
            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAuthority("SCOPE_book:delete");
            auth.anyRequest().authenticated();
        });
        http.oauth2ResourceServer(
                oauth2 ->
                oauth2.jwt(
                      jwt->jwt.jwtAuthenticationConverter(
                      jwtAuthenticationConverter()
                 )
              )
        );
        return http.build();
    }
    private static  final String[] AUTH_WHITELIST =
            {
                    "/api/v1/auth/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yml",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
            };
    @Bean
    @Primary
    public JwtDecoder accessTokenJwtDecoder(){
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getAccessTokenPublicKey())
                .build();
    }

    @Bean
    @Qualifier("refreshTokenJwtDecoder")
    public JwtDecoder refreshTokenJwtDecoder(){
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getRefreshTokenPublicKey())
                .build();
    }

    @Bean
    @Primary
    public JwtEncoder accessTokenJwtEncoder(){
        JWK jwk = new RSAKey
                .Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .build();
        JWKSet jwkSet = new JWKSet(jwk);

        JWKSource<SecurityContext> jwkSource =
                ((jwkSelector, context) -> jwkSelector.select(jwkSet));

        return new NimbusJwtEncoder(jwkSource);

    }
    @Bean
    @Qualifier("refreshTokenJwtEncoder")
    public JwtEncoder refreshTokenJwtEncoder(){
        JWK jwk = new RSAKey
                .Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(jwk);

        JWKSource<SecurityContext> jwkSource =
                ((jwkSelector, context) -> jwkSelector.select(jwkSet));

        return new NimbusJwtEncoder(jwkSource);
    }



    //step 3: create JWKSource bean 
    /*@Bean
    public JWKSource<SecurityContext> jwkSource(){
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }
    // use for verifying the JWT token
    @Bean
    public JwtDecoder jwtDecoder(){
           try {
               return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
           } catch (JOSEException e) {
              throw new RuntimeException(e);
           }
       }
    //step 4: Use JWKSource for Encoding -->use for create jwt token
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource());
    }*/


}

































//Note: How to configure the OAuth2 Resource Server to accept JWTs signed with the RS256 algorithm

        // http.oauth2ResourceServer(oauth2 ->
        //       oauth2.jwt((jwt)->jwt.decoder(null))
        // );