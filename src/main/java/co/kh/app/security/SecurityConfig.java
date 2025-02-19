package co.kh.app.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RSAKey  rsaKey;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(token -> token.disable());
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/anonymous/**","/api/v1/auth/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAuthority("SCOPE_book:read");
            auth.requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAuthority("SCOPE_book:write");
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAuthority("SCOPE_book:update");
            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAuthority("SCOPE_book:delete");
            auth.anyRequest().authenticated();
        });
        http.oauth2ResourceServer(oauth2 ->
              oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder()))
        );
        return http.build();
    }
    //step 3: create JWKSource bean 
    @Bean
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

    }
    
}

































//Note: How to configure the OAuth2 Resource Server to accept JWTs signed with the RS256 algorithm

        // http.oauth2ResourceServer(oauth2 ->
        //       oauth2.jwt((jwt)->jwt.decoder(null))
        // );