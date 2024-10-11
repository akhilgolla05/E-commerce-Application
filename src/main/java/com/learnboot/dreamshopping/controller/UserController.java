package com.learnboot.dreamshopping.controller;

import com.learnboot.dreamshopping.dto.UserDto;
import com.learnboot.dreamshopping.exception.AlreadyExistsException;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.request.CreateUserRequest;
import com.learnboot.dreamshopping.request.UserUpdateRequest;
import com.learnboot.dreamshopping.response.ApiResponse;
import com.learnboot.dreamshopping.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable long userId){

        try {
            UserDto user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){

        try {
            UserDto user = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("User Craated Successfully", user));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,
                                                  @PathVariable long userId){
        try {
            UserDto user = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable long userId){
        try {
           userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }




}
