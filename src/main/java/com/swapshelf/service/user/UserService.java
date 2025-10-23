package com.swapshelf.service.user;

import com.swapshelf.entity.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    public List<User> findAll();

    public List<User> searchUsers(String search);

    public void deleteUser(Long id);


}
