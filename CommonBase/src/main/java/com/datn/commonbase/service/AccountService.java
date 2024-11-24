package com.datn.commonbase.service;


import com.datn.commonbase.entity.Account;

import java.util.List;

public interface AccountService {
    public Account saveAccount(Account account);

    public boolean isExist(Account account);

    public Account saveAccount(String email, String password, String userName, String phoneNumber);

    public boolean updateAccount(long accountId, Account account);

    public boolean changePassword(long accountId, String password, String newPass, String submitPass);

    public Account getAccountById(long accountId);

    public List<Account> getAccounts(int role, int limit);

    public List<Long> getAccountIds(int role, int limit, boolean isActive);


    public Long countAlluser();

    public Long countActiveUser();

    public boolean deleteAccount(long accountId);
}
