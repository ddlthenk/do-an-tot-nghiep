package com.datn.commonbase.repository;


import com.datn.commonbase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //    Page<User> findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "seatNumber")));
    User findUserByUserId(Long id);

    //    List<User> getAllByAccount_AccountId(List<Long> accountIds);
    List<User> findAllByUserIdInOrderByUserIdDesc(List<Long> accountIds);
//    List<User> findAllByUserIdInAndOrderByUserIdAsc(List<Long> accountIds);
//    List<User> getAllByAccount_RoleAnd
//    boolean save(User user);
//    User findUserByEmail(String email);
}
