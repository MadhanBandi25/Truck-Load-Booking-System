package com.truckbooking.truck_booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[6-9]{1}[0-9]{9}$", message = "Enter a valid 10-digit Indian phone number")
    private String phone;

    @Email(message = "Enter a valid email address")
    private String email;
}
