package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImplService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public UserDetailsImplService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.getUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User: " + username + "not found");
        return user;
    }
}
