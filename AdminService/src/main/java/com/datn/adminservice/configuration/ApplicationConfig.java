package com.datn.adminservice.configuration;


import com.datn.adminservice.common.PasswordHandler;
import com.datn.commonbase.entity.Account;
import com.datn.commonbase.entity.User;
import com.datn.commonbase.repository.AccountRepository;
import com.datn.commonbase.repository.UserRepository;
import com.datn.commonbase.util.RoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Date;

@Configuration
//@EnableWebMvc
public class ApplicationConfig implements WebMvcConfigurer {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/**")
                .addResourceLocations("/resources/static/");
    }

    @Bean
    CommandLineRunner addNewAccount() {
        return args -> {
            Account account = Account.builder()
                    .accountId(1L)
                    .email("nguyentrongtam2x2@gmail.com")
                    .role(RoleConstant.ROOT.getValue())
                    .phoneNumber("0328419491")
                    .userName("root")
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            Account account2 = Account.builder()
                    .accountId(2L)
                    .email("nam@com.vn")
                    .userName("admin2")
                    .role(RoleConstant.ADMIN.getValue())
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            Account account3 = Account.builder()
                    .accountId(3L)
                    .userName("admin")
                    .email("testAdmin@cas.vn")
                    .role(RoleConstant.ADMIN.getValue())
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            if (!accountRepository.existsAccountByEmail((account.getEmail()))) {
                account = accountRepository.save(account);
            } else {
                System.out.println("Account with email " + account.getEmail() + " exist");
            }
            if (!accountRepository.existsAccountByEmail((account2.getEmail()))) {
                account2 = accountRepository.save(account2);
            } else {
                System.out.println("Account with email " + account2.getEmail() + " exist");
            }
            if (!accountRepository.existsAccountByEmail((account3.getEmail()))) {
                account3 = accountRepository.save(account3);
            } else {
                System.out.println("Account with email " + account3.getEmail() + " exist");
            }

            User new1 = User.builder()
                    .userId(account.getAccountId())
                    .name("Nguyen Trong Tu Tam")
                    .dateOfBirth(Date.valueOf("2023-12-23"))
                    .language("vi")
                    .build();
            User new2 = User.builder()
                    .userId(account2.getAccountId())
                    .name("Hoang Duc Nam")
                    .language("en")
                    .build();
            User new3 = User.builder()
                    .userId(account3.getAccountId())
                    .name("test")
                    .language("en")
                    .build();

            saveUser(new1);
            saveUser(new2);
            saveUser(new3);

        };
    }


    private boolean saveUser(User user) {
        if (userRepository.save(user) != null) {
            System.err.println("saved user");
            return true;
        } else {
            System.err.println("couldn't save user");
            return false;
        }
    }
}
