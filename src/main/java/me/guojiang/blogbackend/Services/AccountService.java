package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.User;
import me.guojiang.blogbackend.Models.Views.AccountInfoView;
import me.guojiang.blogbackend.Models.Views.SignUpView;
import me.guojiang.blogbackend.Repositories.RoleRepository;
import me.guojiang.blogbackend.Repositories.UserRepository;
import me.guojiang.blogbackend.Security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AccountService(UserRepository userRepository, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JsonResult<Map<String, String>> signUp(SignUpView view) {
        if (userRepository.existsByUsername(view.getUsername())) {
            log.error(view.getUsername() + "already exist");
            return new JsonResult<>(Map.of("username", view.getUsername()))
                    .setMessage(view.getUsername() + " already exist")
                    .setStatus(400);
        }
        var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var registerUser = new User(view.getUsername(), passwordEncoder.encode(view.getPassword()),
                List.of(roleRepository.findByRole("ROLE_USER")));
        userRepository.save(registerUser);
        var token = jwtTokenProvider.generateToken(view.getUsername(),
                userRepository.getUserByUsername(view.getUsername()).getAuthorities());
        return new JsonResult<>(Map.of("username", view.getUsername(), "token", token))
                .setMessage("account has been created!").setStatus(200);
    }

    public String getCurrentUser(String token) {
        try {
            var isValid = jwtTokenProvider.validateToken(token);
            if (isValid) {
                return jwtTokenProvider.getUsername(token);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public User getUserDetail(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User getUserInfoByName(String userName) {
        return userRepository.getUserByUsername(userName);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void convertAccountInfoToExistAccount(AccountInfoView infoView) {

    }

}
