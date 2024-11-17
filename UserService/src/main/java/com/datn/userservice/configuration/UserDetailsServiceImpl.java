package com.datn.userservice.configuration;


import com.datn.commonbase.entity.Account;
import com.datn.commonbase.repository.AccountRepository;
import com.datn.commonbase.repository.UserRepository;
import com.datn.commonbase.util.RoleConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    private static final Logger _log = LogManager.getLogger(UserDetailsServiceImpl.class);

    UserDetailsServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        if (isEmail(username)) {
            account = accountRepository.getAccountByEmail(username);
        } else if (isPhoneNumber(username)) {
            account = accountRepository.getAccountByPhoneNumber(username);
        } else {
            account = accountRepository.getAccountByUserName(username);
        }
        if (account != null && account.getRole() == RoleConstant.USER.getValue()) {
            return buildUserForAuthentication(account);
        }
        return null;
    }

    private UserDetails buildUserForAuthentication(Account account) {
        try {
            return org.springframework.security.core.userdetails.User.builder().username(String.valueOf(account.getAccountId()))
                    .password(account.getPassword())
                    .roles(RoleConstant.getObjectName(account.getRole()))
                    .authorities(RoleConstant.getObjectName(account.getRole())).build();
        } catch (Exception e) {
            _log.error(e.getMessage());
            return null;
        }

    }

    public static boolean isEmail(String input) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isPhoneNumber(String input) {
        String regex = "^[0-9]{10}$"; // Kiểm tra xem có 10 chữ số không
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
