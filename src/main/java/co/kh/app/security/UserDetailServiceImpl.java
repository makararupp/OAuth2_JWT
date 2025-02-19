package co.kh.app.security;

import co.kh.app.entities.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import co.kh.app.entities.Role;
import co.kh.app.entities.User;
import co.kh.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService{
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //create object user........
        System.out.println("load");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with this email is not found..!")
        );

        System.out.println("user role:" +user.getRoles());
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            for (Authority authority : role.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
        }

        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        authorities);
        return securityUser;
    }

}
