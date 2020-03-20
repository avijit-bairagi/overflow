package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.utils.Constants;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.repository.UserRepository;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import com.mrrobot.overflow.security.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest servletRequest;

    @Autowired
    JwtProvider jwtProvider;

    @Override
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserData getUserData() {

        return jwtProvider.getUserData(servletRequest.getHeader(Constants.AUTH_HEADER));
    }
}
