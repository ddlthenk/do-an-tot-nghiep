package com.datn.commonbase.service.implement;


import com.datn.commonbase.dto.UserDto;
import com.datn.commonbase.entity.User;
import com.datn.commonbase.repository.AccountRepository;
import com.datn.commonbase.repository.UserRepository;
import com.datn.commonbase.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    private final Logger _log = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(Long id) {
        try {
            User user = userRepository.findUserByUserId(id);
            return user;
        } catch (NullPointerException e) {
            _log.error(e.getMessage());
            return null;
        }

    }

    @Override
    public Map<Long, User> getMapUsers(List<Long> userIds) {
        Map<Long, User> productMap = new HashMap<>();
        for (long id : userIds) {
            User user = getUserById(id);
            if (user != null) {
                productMap.put(id, user);
            }
        }
        if (productMap.isEmpty()) {
            return Collections.emptyMap();
        }
        return productMap;
    }

    @Override
    public User getUserByEmail(String email) {
//        try {
//            User user = userRepository.findUserByEmail(email);
//            return user;
//        }
//        catch (NullPointerException e){
//            System.err.println(e.getMessage());
//
//        }
        return null;
    }

    @Override
    public List<User> getUsers(int page, int pageSize) {
        try {
            Page<User> pageUser = userRepository.findAll(PageRequest.of(0, 10));
            System.out.println(pageUser.get());
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getUsersByRole(int page, int pageSize, int role) {
        try {
            List<Long> accountList = accountRepository.getAccountIds(role, true, pageSize);
            List<User> userList = userRepository.findAllByUserIdInOrderByUserIdDesc(accountList);
            if (userList != null && !userList.isEmpty()) {
                return userList;
            }
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<User> getUsersByListIds(List<Long> accountIds) {
        try {
            List<User> userList = userRepository.findAllByUserIdInOrderByUserIdDesc(accountIds);
            if (userList == null || userList.isEmpty()) {
                return null;
            }
            return userList;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserDto> getUsersDtoByListIds(List<Long> accountIds) {
        try {
            List<User> userList = userRepository.findAllByUserIdInOrderByUserIdDesc(accountIds);
            if (userList == null || userList.isEmpty()) {
                return Collections.emptyList();
            }
            List<UserDto> userDtos = new ArrayList<>();
            for (User user : userList) {
                UserDto userDto = UserDto.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .language(user.getLanguage())
                        .cash(user.getCash())
                        .point(user.getPoint())
                        .build();
                userDtos.add(userDto);
            }
            return userDtos;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateUser(long userId, User user) {
        try {
            User exitsUser = userRepository.findUserByUserId(userId);
            if (exitsUser == null) {
                return false;
            }
            if (user.getName() != null && !user.getName().isEmpty()) {
                exitsUser.setName(user.getName());
            }
            if (user.getDateOfBirth() != null) {
                exitsUser.setDateOfBirth(user.getDateOfBirth());
            }
            userRepository.save(exitsUser);
        } catch (Exception e) {
            _log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
