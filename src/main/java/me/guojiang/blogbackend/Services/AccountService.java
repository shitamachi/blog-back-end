package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.User;
import me.guojiang.blogbackend.Models.Views.AccountInfoView;
import me.guojiang.blogbackend.Models.Views.SignUpView;
import me.guojiang.blogbackend.Repositories.RoleRepository;
import me.guojiang.blogbackend.Repositories.UserRepository;
import me.guojiang.blogbackend.Security.providers.JwtTokenProvider;
import me.guojiang.blogbackend.Utils.VerifyCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final VerifyCodeUtil verifyCodeUtil;

    public AccountService(UserRepository userRepository, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider, EmailService emailService, VerifyCodeUtil verifyCodeUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.verifyCodeUtil = verifyCodeUtil;
    }

    public JsonResult<Map<String, String>> signUp(SignUpView view) {
        var username = view.getUsername();
        var password = view.getPassword();
        var email = view.getEmail();

        if (userRepository.existsByUsername(username)) {
            log.error(view.getUsername() + "already exist");
            return new JsonResult<>(Map.of("username", username))
                    .setMessage(view.getUsername() + " already exist")
                    .setStatus(400);
        }
        var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var registerUser = new User(username, passwordEncoder.encode(password),
                List.of(roleRepository.findByRole("ROLE_USER")));
        registerUser.setEmail(email);
        try {
            if (emailService.sendCodeAndSave(username, email).get()) {
                userRepository.save(registerUser);
                return JsonResult.Ok(Map.of("data", ""));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(Map.of("", "")).setMessage("has not send verify code email,please try again").setStatus(500);
    }

    public JsonResult<Map<String, String>> verifyAccount(String username, String email, String code) {
        if (verifyCodeUtil.verifyCode(username, email, code)) {
            var verifyUser = userRepository.getUserByUsername(username);
            verifyUser.setEnabled(true);
            userRepository.save(verifyUser);
            return JsonResult.Ok(Map.of("data", "you account has been verified"));
        }
        return JsonResult.BadRequest(null);
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
