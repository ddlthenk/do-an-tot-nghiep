package com.devtam.userservice.configuration;

import com.devtam.commonbase.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    DataSource dataSource;

    private static final Logger _log = LogManager.getLogger(SecurityConfiguration.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                // configure access rules of static resources
                                .requestMatchers("/css/**", "/js/**", "/img/**", "/webfonts/**").permitAll()

                                // configure access rules of unauthenticated users
                                .requestMatchers("/login", "/logout").permitAll()
                                .requestMatchers("/home", "/**").permitAll()
                                .requestMatchers("/personal", "/cart").hasAuthority("USER")
                                // configure access rules of admin users
//                        .requestMatchers("/admin/**", "/admin/", "/index").hasAnyAuthority("ADMIN", "ROOT")
//                        .requestMatchers("/admin/employee/**").hasAuthority("ROOT")

                                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?login-error=true")
                        .defaultSuccessUrl("/home", true)
                        .successHandler(new MySavedRequestAwareHandler())
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {
                            _log.error("Logout handler");
                        })
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(7 * 24 * 60 * 60))
                .exceptionHandling(e -> e
                        .accessDeniedHandler((httpServletRequest, httpServletResponse, e1) -> {
                            httpServletResponse.sendRedirect("/access-denied");
                        }));
        return http.build();
    }

    public static class RefererRedirectionAuthenticationSuccessHandler
            implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            String refererUrl = request.getHeader("Referer");
            response.sendRedirect(refererUrl != null ? refererUrl : "/home");
        }
    }

    public class MySavedRequestAwareHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        @Override
        protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
            return super.determineTargetUrl(request, response);
        }
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set your configuration on the auth object
//        auth.jdbcAuthentication().dataSource(dataSource);
//                .usersByUsernameQuery("select email, password from tbl_user where email = ?");
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    public JdbcTokenRepositoryImpl persistentTokenRepository() {
        JdbcTokenRepositoryImpl memory = new JdbcTokenRepositoryImpl();
        memory.setDataSource(dataSource);
        _log.error("Persistent token repository created");
        return memory;
    }

}
