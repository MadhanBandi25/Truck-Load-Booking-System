package com.truckbooking.truck_booking.dto.request;


import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.TruckType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class LoadRequest {

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Drop location is required")
    private String dropLocation;

    @NotNull(message = "Cargo type is required")
    private CargoType cargoType;

    @NotNull(message = "Weight is required")
    @Min(value = 1, message = "Weight must be at least 1 ton")
    private Double weight;

    @NotNull(message = "Distance is required")
    @Min(value = 1, message = "Distance must be at least 1 km")
    private Double distance;

    @NotNull(message = "Truck type is required")
    private TruckType truckType;

    @FutureOrPresent(message = "Pickup date must be today or future")
    @NotNull(message = "Pickup date is required")
    private LocalDate pickupDate;


    @NotNull(message = "Pickup time is required")
    private LocalTime pickupTime;

    @Size(max = 500, message = "Description too long")
    private String description;
}
