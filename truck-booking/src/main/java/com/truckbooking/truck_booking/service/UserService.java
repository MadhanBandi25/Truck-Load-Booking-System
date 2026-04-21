package com.truckbooking.truck_booking.service;

import com.truckbooking.truck_booking.dto.request.ChangePasswordRequest;
import com.truckbooking.truck_booking.dto.request.LoginRequest;
import com.truckbooking.truck_booking.dto.request.RegisterRequest;
import com.truckbooking.truck_booking.dto.request.UpdateProfileRequest;
import com.truckbooking.truck_booking.dto.response.UserResponse;
import java.util.List;

public interface UserService {

    UserResponse register(RegisterRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();

    List<UserResponse> searchByEmail(String email);
    List<UserResponse> searchByName(String name);
    List<UserResponse> searchByPhone(String phone);

    UserResponse updateProfile(Long id, UpdateProfileRequest request);
    UserResponse login(LoginRequest request);

    void  changePassword(Long id, ChangePasswordRequest request);

    void deleteUser(Long id);
    void activateUser(Long id);
}
