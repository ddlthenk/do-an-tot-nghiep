package com.datn.commonbase.service;

import com.datn.commonbase.dto.UserDto;
import com.datn.commonbase.entity.User;

import java.util.List;
import java.util.Map;


public interface UserService {
    public User getUserById(Long id);

    public Map<Long, User> getMapUsers(List<Long> userIds);

    public User getUserByEmail(String email);

    public List<User> getUsers(int page, int pageSize);

    public List<User> getUsersByRole(int page, int pageSize, int role);

    public List<User> getUsersByListIds(List<Long> accountIds);

    public boolean updateUser(long userId, User user);

    public User saveUser(User user);

    public List<UserDto> getUsersDtoByListIds(List<Long> accountIds);

}
