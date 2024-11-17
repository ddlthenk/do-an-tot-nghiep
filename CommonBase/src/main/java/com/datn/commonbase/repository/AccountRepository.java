package com.datn.commonbase.repository;


import com.datn.commonbase.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByEmail(String email);

    Account getAccountByEmailAndRole(String email, int role);

    Account getAccountByPhoneNumber(String phoneNumber);

    Account getAccountByPhoneNumberAndRole(String phoneNumber, int role);


    Account getAccountByAccountId(Long accountId);

    Account getAccountByUserName(String userName);

    Account getAccountByUserNameAndRole(String userName, int role);

    boolean existsAccountByUserName(String userName);

    boolean existsAccountByUserNameAndRole(String userName, int role);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByEmailAndRole(String email, int role);

    boolean existsAccountByPhoneNumber(String phoneNumber);

    boolean existsAccountByPhoneNumberAndRole(String phoneNumber, int role);

    boolean existsAccountByAccountId(Long accountId);

    List<Account> getAllByRole(int role, Pageable pageable);

    Page<Account> findAll(Pageable pageable);

    Page<Account> findAllByRole(Pageable pageable, int role);

    @Query(value = "SELECT tbl_account.account_id FROM tbl_account where tbl_account.role = ? and tbl_account.enable = ? ORDER BY tbl_account.account_id DESC limit ?", nativeQuery = true)
    List<Long> getAccountIds(int role, boolean enable, int limit);

    @Query(value = "SELECT tbl_account.account_id FROM tbl_account where tbl_account.enable = ? ORDER BY tbl_account.account_id DESC limit ?", nativeQuery = true)
    List<Long> getAccountIds(boolean enable, int limit);

    Long countAccountsByEnable(boolean status);

    Long countAccountsBy();

}
