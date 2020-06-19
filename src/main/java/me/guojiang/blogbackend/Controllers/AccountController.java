package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Models.User;
import me.guojiang.blogbackend.Models.Views.AccountInfoView;
import me.guojiang.blogbackend.Models.Views.PasswordChangeView;
import me.guojiang.blogbackend.Models.Views.SignInView;
import me.guojiang.blogbackend.Models.Views.SignUpView;
import me.guojiang.blogbackend.Repositories.UserRepository;
import me.guojiang.blogbackend.Security.providers.JwtTokenProvider;
import me.guojiang.blogbackend.Services.AccountService;
import me.guojiang.blogbackend.Services.FileService;
import me.guojiang.blogbackend.Services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FileService fileService;

    public AccountController(AuthenticationManager authenticationManager, AccountService accountService,
                             FileService fileService, JwtTokenProvider jwtTokenProvider, UserRepository users, EmailService emailService, Environment env) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = users;
        this.fileService = fileService;
    }

    @PostMapping("/signIn")
    public JsonResult<HashMap<String, Object>> signIn(
            @RequestBody SignInView signInUser,
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal UserDetails user) {
        var username = signInUser.getUsername();
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, signInUser.getPassword()));
        log.debug(String.valueOf(authentication.isAuthenticated()));
        var token = jwtTokenProvider.generateToken(username,
                userRepository.getUserByUsername(username).getAuthorities());
        var model = new HashMap<String, Object>();
        model.put("username", username);
        model.put("token", token);
        model.put("req-cookie", request.getCookies());
        model.put("rep-cookie", response.getHeader("Set-Cookie"));
        model.put("user", user);
        return new JsonResult<>(model).setStatus(200).setMessage("has been signed in");
    }

    @PostMapping("/signUp")
    public JsonResult<Map<String, String>> signUp(@RequestBody SignUpView signUpView) {
        return accountService.signUp(signUpView);
    }

    @PostMapping("signUp/verify")
    public JsonResult<Map<String, String>> verifyCode(@RequestBody Map<String, String> map) {
        var email = map.get("email");
        var code = map.get("code");
        var username = map.get("username");

        return accountService.verifyAccount(username, email, code);
    }

    @GetMapping("/{name}")
    public JsonResult<AccountInfoView> getUserDetail(@PathVariable(value = "name") String userName) {
        var foundUser = accountService.getUserInfoByName(userName);
        if (foundUser != null) {
            return new JsonResult<>(AccountInfoView.createFromUserEntity(foundUser)).setStatus(200).setMessage("found user successfully!");
        }
        return new JsonResult<>(new AccountInfoView()).setStatus(404).setMessage("didn't find any user");
    }

    @GetMapping()
    public JsonResult<AccountInfoView> getCurrentAccount(@RequestHeader(name = "Authorization") String headerStr) {
        var token = jwtTokenProvider.resolveToken(headerStr);
        var currentUserName = accountService.getCurrentUser(token);
        if (currentUserName != null) {
            var currentUser = AccountInfoView.createFromUserEntity(accountService.getUserInfoByName(currentUserName));
            return new JsonResult<>(currentUser).setStatus(200).setMessage("successful!");
        }
        return new JsonResult<>(new AccountInfoView()).setStatus(404).setMessage("didn't find any user");
    }

    @PutMapping()
    public JsonResult<User> updateUserInfo(@RequestBody AccountInfoView newAccountInfo) {
        //TODO 进行校验
        log.debug(SecurityContextHolder.getContext().getAuthentication().toString());
        var currentUser = accountService.getUserInfoByName(newAccountInfo.getUserName());
        currentUser.setEmail(newAccountInfo.getEmail());
        currentUser.setUsername(newAccountInfo.getUserName());
        currentUser.setNickname(newAccountInfo.getNickName());
        currentUser.setAvatar(newAccountInfo.getAvatar());
        currentUser.setUpdateTime(LocalDateTime.now());
        var updatedUser = accountService.updateUser(currentUser);
        return new JsonResult<>(updatedUser).setStatus(200).setMessage("ok!");
    }

    @PostMapping("/password")
    public JsonResult<Map<String, String>> updatePassword(@RequestBody PasswordChangeView passwordChangeView) {
        var currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var pwEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (!pwEncoder.matches(passwordChangeView.getOldPassword(), currentUser.getPassword())) {
            return new JsonResult<>(Map.of("userName", currentUser.getUsername())).setMessage("旧密码不匹配").setStatus(400);
        }
        var user = userRepository.getUserByUsername(currentUser.getUsername());
        user.setPassword(pwEncoder.encode(passwordChangeView.getNewPassword()));
        if (userRepository.save(user) != null) {
            return new JsonResult<>(Map.of("userName", user.getUsername())).setMessage("修改成功").setStatus(200);
        }
        return new JsonResult<>(Map.of("userName", user.getUsername())).setMessage("修改密码错误").setStatus(400);
    }

    @PostMapping("/{name}/avatar")
    public JsonResult<Map<Object, Object>> setAvatar(@PathVariable String name,
                                                     @RequestParam("file") MultipartFile file) {
        var authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!authUser.getUsername().equals(name))
            return new JsonResult<Map<Object, Object>>().setStatus(404).setMessage("wrong user");
        var path = fileService.store(file);
        authUser.setAvatar(path);
        accountService.updateUser(authUser);
        return new JsonResult<Map<Object, Object>>(Map.of("path", path)).setStatus(200).setMessage("upload ok!");
    }

//    for develop
//    @GetMapping("/currentuser")
//    public Object getCurrentUser() {
//        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
}
