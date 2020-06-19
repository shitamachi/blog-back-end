package me.guojiang.blogbackend.Config;

import me.guojiang.blogbackend.Security.JwtAuthenticationEntryPoint;
import me.guojiang.blogbackend.Security.PersistentJwtTokenBasedRememberMeServices;
import me.guojiang.blogbackend.Security.filters.JwtAuthorizationTokenFilter;
import me.guojiang.blogbackend.Security.filters.JwtUsernamePasswordAuthenticationFilter;
import me.guojiang.blogbackend.Security.filters.ProceedingRememberMeAuthenticationFilter;
import me.guojiang.blogbackend.Security.handlers.JwtAccessDeniedHandler;
import me.guojiang.blogbackend.Security.handlers.JwtAuthenticationFailureHandler;
import me.guojiang.blogbackend.Security.handlers.JwtAuthenticationSuccessHandler;
import me.guojiang.blogbackend.Security.providers.JwtTokenProvider;
import me.guojiang.blogbackend.Services.UserDetailsImplService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.remember-me-security-key}")
    private String rememberMeSecurityKey;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsImplService userDetailsImplService;
    private final DataSource dataSource;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          UserDetailsImplService userDetailsImplService, DataSource dataSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsImplService = userDetailsImplService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(jwtUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(rememberMeAuthenticationFilter(authenticationManager(),
                        persistentJwtTokenBasedRememberMeServices()), RememberMeAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationTokenFilter(jwtTokenProvider),
                        JwtUsernamePasswordAuthenticationFilter.class)
//                .authenticationProvider(rememberMeAuthenticationProvider())
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .userDetailsService(userDetailsImplService)
                .rememberMeServices(persistentJwtTokenBasedRememberMeServices())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .and()
                .logout()
                .logoutUrl("/signOut")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/img/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/articles").permitAll()
                .antMatchers(HttpMethod.GET, "/api/tags/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/account/**").permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsImplService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter() throws Exception {
        var filter = new JwtUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
        filter.setRememberMeServices(persistentJwtTokenBasedRememberMeServices());
        return filter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices) {
        return new ProceedingRememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(rememberMeSecurityKey);
    }

    @Bean
    public PersistentTokenRepository jdbcTokenRepository() {
        var jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public RememberMeServices persistentJwtTokenBasedRememberMeServices() {
        return new PersistentJwtTokenBasedRememberMeServices(
                rememberMeSecurityKey,
                userDetailsImplService,
                jdbcTokenRepository());
    }

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}
