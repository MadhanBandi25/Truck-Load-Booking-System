package com.truckbooking.truck_booking.controller;

import com.truckbooking.truck_booking.dto.request.ChangePasswordRequest;
import com.truckbooking.truck_booking.dto.request.LoginRequest;
import com.truckbooking.truck_booking.dto.request.RegisterRequest;
import com.truckbooking.truck_booking.dto.request.UpdateProfileRequest;
import com.truckbooking.truck_booking.dto.response.UserResponse;
import com.truckbooking.truck_booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request){
        UserResponse response = userService.login(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/name")
    public ResponseEntity<List<UserResponse>> searchByName(@RequestParam String name){
        return ResponseEntity.ok(userService.searchByName(name));
    }

    @GetMapping("/email")
    public ResponseEntity<List<UserResponse>> searchByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.searchByEmail(email));
    }

    @GetMapping("/phone")
    public ResponseEntity<List<UserResponse>> getByPhone(@RequestParam String phone){
        return ResponseEntity.ok(userService.searchByPhone(phone));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id,@Valid @RequestBody UpdateProfileRequest request){
        return ResponseEntity.ok(userService.updateProfile(id,request));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long id,@RequestBody ChangePasswordRequest request){

        userService.changePassword(id, request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");

    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        userService.activateUser(id);
        return ResponseEntity.ok("User activated successfully");
    }
}
