package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.utils.Constants;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.repository.UserRepository;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import com.mrrobot.overflow.security.model.UserData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest servletRequest;

    @Autowired
    ProfileService profileService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ModelMapper modelMapper;

    @Value("${user.defaultUserLimit}")
    int defaultUserLimit;

    @Override
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<ProfileResponse> findAll() {

        List<ProfileResponse> responseList = new ArrayList<>();

        List<User> users = userRepository.findAll();

        users.forEach(user -> {

            Optional<Profile> profileOptional = profileService.findByUserId(user.getId());

            if (profileOptional.isPresent())
                responseList.add(getProfileResponse(user, profileOptional.get()));

        });

        return responseList;
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
    public User update(User user) throws NotFoundException {

        if (userRepository.findById(user.getId()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");
        }

        return userRepository.save(user);
    }

    @Override
    public UserData getUserData() {

        return jwtProvider.getUserData(servletRequest.getHeader(Constants.AUTH_HEADER));
    }

    @Override
    public List<ProfileResponse> findAllByRanking(int parse) {

        Pageable pageable = PageRequest.of(parse, defaultUserLimit, Sort.by("point").descending());

        List<ProfileResponse> responseList = new ArrayList<>();

        List<Profile> profiles = profileService.findAll(pageable);

        profiles.forEach(profile -> {

            Optional<User> userOptional = userRepository.findById(profile.getUserId());

            if (userOptional.isPresent())
                responseList.add(getProfileResponse(userOptional.get(), profile));
        });

        return responseList;
    }

    private ProfileResponse getProfileResponse(User user, Profile profile) {

        ProfileResponse response = modelMapper.map(profile, ProfileResponse.class);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}
