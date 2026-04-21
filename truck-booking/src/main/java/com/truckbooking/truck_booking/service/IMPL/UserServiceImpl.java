package com.truckbooking.truck_booking.service.IMPL;

import com.truckbooking.truck_booking.dto.request.ChangePasswordRequest;
import com.truckbooking.truck_booking.dto.request.LoginRequest;
import com.truckbooking.truck_booking.dto.request.RegisterRequest;
import com.truckbooking.truck_booking.dto.request.UpdateProfileRequest;
import com.truckbooking.truck_booking.dto.response.UserResponse;
import com.truckbooking.truck_booking.entity.User;
import com.truckbooking.truck_booking.enums.Role;
import com.truckbooking.truck_booking.enums.UserStatus;
import com.truckbooking.truck_booking.exception.DuplicateEntryException;
import com.truckbooking.truck_booking.exception.InvalidStatusException;
import com.truckbooking.truck_booking.exception.ResourceNotFoundException;
import com.truckbooking.truck_booking.exception.UnauthorizedException;
import com.truckbooking.truck_booking.mapper.UserMapper;
import com.truckbooking.truck_booking.repository.UserRepository;
import com.truckbooking.truck_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())){
            throw new DuplicateEntryException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())){
            throw new DuplicateEntryException("Phone already exists");
        }

        User user = UserMapper.toEntity(request);

        if (request.getRole() != null){
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.SHIPPER);
        }

        User saved=  userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> searchByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> searchByPhone(String phone) {
        return userRepository.findByPhoneContaining(phone)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateProfile(Long id, UpdateProfileRequest request) {

        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())
         && !user.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new DuplicateEntryException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())
         && !user.getPhone().equals(request.getPhone())) {
            throw  new DuplicateEntryException("Phone already exists");
        }

        UserMapper.updateEntity(user, request);
        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    public UserResponse login(LoginRequest request) {

        String input = request.getUsername();

        User user ;

        if(input.contains("@")){
            user = userRepository.findByEmailIgnoreCase(input.toLowerCase())
                    .orElseThrow(()-> new UnauthorizedException("Invalid credentials"));
        } else {
            user = userRepository.findByPhone(input)
                    .orElseThrow(()-> new UnauthorizedException("Invalid credentials"));
        }

        if (user.getStatus() != UserStatus.ACTIVE){
            throw new InvalidStatusException("User is not active");
        }

        if (!user.getPassword().equals(request.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }

        return UserMapper.toResponse(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {

        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (!user.getPassword().equals(request.getOldPassword())){
            throw new UnauthorizedException("Old password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Password do not match");
        }
        user.setPassword(request.getNewPassword());

        userRepository.save(user);

    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getDeletedAt() != null) {
            throw new InvalidStatusException("User already deleted");
        }

        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);

    }

    @Override
    public void activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getDeletedAt() == null && user.getStatus() == UserStatus.ACTIVE){
            throw new InvalidStatusException("User is already active");
        }

        user.setDeletedAt(null);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
