package com.truckbooking.truck_booking.mapper;

import com.truckbooking.truck_booking.dto.request.RegisterRequest;
import com.truckbooking.truck_booking.dto.request.UpdateProfileRequest;
import com.truckbooking.truck_booking.dto.response.UserResponse;
import com.truckbooking.truck_booking.entity.User;

import java.util.Locale;

public class UserMapper {

    public static User toEntity(RegisterRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponse toResponse(User user){

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    public static void updateEntity(User user, UpdateProfileRequest request){

        if (request.getName() != null){
            user.setName(request.getName().trim());
        }
        if (request.getPhone() != null){
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() !=null ){
            user.setEmail(request.getEmail().toLowerCase().trim());
        }
    }

}
