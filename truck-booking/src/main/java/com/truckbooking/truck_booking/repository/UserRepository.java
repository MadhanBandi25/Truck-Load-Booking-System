package com.truckbooking.truck_booking.repository;

import com.truckbooking.truck_booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhone(String phone);

    List<User> findByEmailContainingIgnoreCase(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByPhoneContaining(String phone);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailIgnoreCase(String email);
}
