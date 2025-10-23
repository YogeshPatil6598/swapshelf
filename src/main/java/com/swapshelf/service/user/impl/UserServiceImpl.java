package com.swapshelf.service.user.impl;

import com.swapshelf.entity.User;
import com.swapshelf.repository.UserRepository;
import com.swapshelf.service.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
//

//        @Override
//        public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
//            User user = userRepository.findByUsername(username);
//            if (user == null) throw new UsernameNotFoundException("user not found");
//            return new org.springframework.security.core.userdetails.User(
//                    user.getEmail(), user.getPassword(), Collections.emptyList());
//
//
//
//        }
//    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUsers(String search) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }







}

