package com.learnboot.dreamshopping.service.user;

import com.learnboot.dreamshopping.dto.UserDto;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.request.CreateUserRequest;
import com.learnboot.dreamshopping.request.UserUpdateRequest;

public interface IUserService {

    UserDto getUserById(long userId);
    UserDto createUser(CreateUserRequest request);
    UserDto updateUser(UserUpdateRequest request, long userId);
    void deleteUser(long userId);

    User getAuthenticatedUser();
}
