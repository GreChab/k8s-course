package com.epam.k8scourse.userservice.service;

import com.epam.k8scourse.userservice.dto.UserResponse;
import com.epam.k8scourse.userservice.dto.UserRequest;
import com.epam.k8scourse.userservice.model.User;
import com.epam.k8scourse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String STRING_ZERO = "0";

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest userRequest) {
        if (exist(userRequest.getUsername())) {
            User user = User.builder()
                    .username(userRequest.getUsername())
                    .amount(STRING_ZERO)
                    .build();

            userRepository.save(user);
            return mapToUserResponse(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(UserService::mapToUserResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(u -> userRepository.deleteById(u.getId()),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id)
                .map(currentUser -> updateUser(currentUser, userRequest))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private UserResponse updateUser(User currentUser, UserRequest userRequest) {
        if (exist(userRequest.getUsername())) {
            User updatedUser = getUpdatedUser(currentUser, userRequest);
            userRepository.save(updatedUser);
            return mapToUserResponse(updatedUser);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private static User getUpdatedUser(User currentUser, UserRequest userRequest) {
        User.UserBuilder updatedUser = User.builder()
                .id(currentUser.getId())
                .username(userRequest.getUsername());
        if (Objects.nonNull(userRequest.getAmountOfPosts())) {
            updatedUser.amount(userRequest.getAmountOfPosts());
        } else {
            updatedUser.amount(currentUser.getAmount());
        }
        return updatedUser.build();
    }

    private static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .amountOfPosts(user.getAmount())
                .build();
    }

    private static boolean exist(String value) {
        return !Objects.isNull(value) && !value.isBlank();
    }
}
