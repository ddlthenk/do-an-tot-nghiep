package com.datn.commonbase.service.implement;

import com.datn.commonbase.common.PasswordHandler;
import com.datn.commonbase.common.Validator;
import com.datn.commonbase.entity.Account;
import com.datn.commonbase.repository.AccountRepository;
import com.datn.commonbase.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final Logger _log = LogManager.getLogger(AccountServiceImpl.class);

    @Override
    public Account saveAccount(Account account) {
        if (account != null) {
            return accountRepository.save(account);
        }
        return null;
    }

    @Override
    public boolean isExist(Account account) {
        boolean resultEmail = false;
        boolean resultPhone = false;
        if (account.getEmail() != null && !account.getEmail().equals("")) {
            resultEmail = accountRepository.existsAccountByEmail(account.getEmail());
        }
        if (account.getPhoneNumber() != null && !account.getPhoneNumber().equals("")) {
            resultPhone = accountRepository.existsAccountByPhoneNumber(account.getPhoneNumber());
        }
        return (resultEmail || resultPhone);
    }

    @Override
    public Account saveAccount(String email, String password, String userName, String phoneNumber) {
        Account newAccount = Account.builder()
                .email(email)
                .password(password)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .build();
        return null;
    }

    @Override
    public boolean updateAccount(long accountId, Account account) {
        try {
            Account exitsAccount = accountRepository.getAccountByAccountId(accountId);
            if (exitsAccount == null) {
                return false;
            }
            if (account.getUserName() != null && !account.getUserName().isEmpty()) {
                exitsAccount.setUserName(account.getUserName());
            }
            if (account.getEmail() != null && Validator.isEmailValid(account.getEmail())) {
                exitsAccount.setEmail(account.getEmail());
            }
            if (account.getPhoneNumber() != null && Validator.isPhoneNumberValid(account.getPhoneNumber())) {
                exitsAccount.setPhoneNumber(account.getPhoneNumber());
            }
            accountRepository.save(exitsAccount);
        } catch (Exception e) {
            _log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean changePassword(long accountId, String password, String newPass, String submitPass) {
        Account account = accountRepository.getAccountByAccountId(accountId);
        if (account == null) {
            return false;
        }
        if (newPass.compareTo(submitPass) == 0) {
            if (PasswordHandler.checkPassword(password, account.getPassword())) {
                account.setPassword(PasswordHandler.hashPassword(newPass));
                saveAccount(account);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Account getAccountById(long accountId) {
        if (accountId >= 0) {
            Account account = accountRepository.getAccountByAccountId(accountId);
            return account;
        }
        return null;
    }

    @Override
    public List<Account> getAccounts(int role, int limit) {
        List<Account> accounts;
        Pageable pageable = PageRequest.of(0, limit);
        try {
            if (role >= 0 && role <= 2) {
                accounts = accountRepository.getAllByRole(role, pageable);
            } else {
                Page<Account> page = accountRepository.findAll(pageable);
                accounts = page.getContent();
            }
            return accounts;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Long> getAccountIds(int role, int limit, boolean isActive) {
        List<Long> accountIds;
        try {
            if (role >= 0 && role <= 2) {
                accountIds = accountRepository.getAccountIds(role, isActive, limit);
            } else {
                accountIds = accountRepository.getAccountIds(isActive, limit);
            }
            return accountIds;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Long countAlluser() {
        Long count = accountRepository.countAccountsBy();
        return count;
    }

    @Override
    public Long countActiveUser() {
        Long count = accountRepository.countAccountsByEnable(true);
        return count;
    }

    @Override
    public boolean deleteAccount(long accountId) {
        try {
            accountRepository.deleteById(accountId);
            return true;
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return false;
    }
}
