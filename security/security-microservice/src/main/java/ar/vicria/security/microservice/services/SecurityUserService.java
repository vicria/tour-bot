package ar.vicria.security.microservice.services;

import ar.vicria.security.microservice.configurations.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return loadUserById(1);
    }


    public UserDetails loadUserById(Integer id) {
        return SecurityUser.create(1, "name", "passw0rd");
    }
}
