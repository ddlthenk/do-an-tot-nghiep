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
                    .email("nguyenvanthanh@gmail.com")
                    .role(RoleConstant.ROOT.getValue())
                    .phoneNumber("0393002963")
                    .userName("root")
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            Account account2 = Account.builder()
                    .accountId(2L)
                    .email("thanhAdmin@gmail.com")
                    .userName("admin")
                    .role(RoleConstant.ADMIN.getValue())
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            Account account3 = Account.builder()
                    .accountId(3L)
                    .userName("user")
                    .email("thanhUser@gmail.com")
                    .role(RoleConstant.USER.getValue())
                    .password(PasswordHandler.hashPassword("123456"))
                    .build();
            if (!accountRepository.existsAccountByEmail((account.getEmail()))) {
                account = accountRepository.save(account);
            } else {
                account = accountRepository.getAccountByUserName(account.getUserName());
                System.out.println("Account with email " + account.getEmail() + " exist");
            }
            if (!accountRepository.existsAccountByEmail((account2.getEmail()))) {
                account2 = accountRepository.save(account2);
            } else {
                account2 = accountRepository.getAccountByUserName(account2.getUserName());
                System.out.println("Account with email " + account2.getEmail() + " exist");
            }
            if (!accountRepository.existsAccountByEmail((account3.getEmail()))) {
                account3 = accountRepository.save(account3);
            } else {
                account3 = accountRepository.getAccountByUserName(account3.getUserName());
                System.out.println("Account with email " + account3.getEmail() + " exist");
            }

            User new1 = User.builder()
                    .userId(account.getAccountId())
                    .name("Nguyen Van Thanh")
                    .dateOfBirth(Date.valueOf("2023-12-23"))
                    .language("vi")
                    .build();
            User new2 = User.builder()
                    .userId(account2.getAccountId())
                    .dateOfBirth(Date.valueOf("2023-12-23"))
                    .name("Thanh Nguyen Van")
                    .language("en")
                    .build();
            User new3 = User.builder()
                    .userId(account3.getAccountId())
                    .name("User Thanh")
                    .dateOfBirth(Date.valueOf("2023-12-23"))
                    .language("vi")
                    .build();
            saveUser(new1);
            saveUser(new2);
            saveUser(new3);

        };
    }


    private boolean saveUser(User user) {
        try {
            if (!userRepository.existsById(user.getUserId())) {
                userRepository.save(user);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
