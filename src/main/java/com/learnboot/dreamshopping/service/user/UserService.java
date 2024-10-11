package com.learnboot.dreamshopping.service.user;

import com.learnboot.dreamshopping.dto.UserDto;
import com.learnboot.dreamshopping.exception.AlreadyExistsException;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.repository.UserRepository;
import com.learnboot.dreamshopping.request.CreateUserRequest;
import com.learnboot.dreamshopping.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto)
                .orElseThrow(()-> new RuntimeException("User Not Found"));
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return convertToDto(userRepository.save(user));
                }).orElseThrow(()-> new AlreadyExistsException(request.getEmail()+" Already Exists"));
    }

    @Override
    public UserDto updateUser(UserUpdateRequest request, long userId) {
        return userRepository.findById(userId)
                .map(existingUser-> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return convertToDto(userRepository.save(existingUser));
                }).orElseThrow(()-> new ResourceNotFoundException("User Not Found!"));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                     throw new ResourceNotFoundException("User Not Found");
                });

    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    private UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
