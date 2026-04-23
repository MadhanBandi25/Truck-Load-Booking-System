package com.truckbooking.truck_booking.dto.response;

import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class LoadResponse {


    private Long id;
    private Long shipperId;

    private String pickupLocation;
    private String dropLocation;

    private CargoType cargoType;
    private Double weight;
    private Double distance;
    private TruckType truckType;

    private LocalDate pickupDate;
    private LocalTime pickupTime;

    private BigDecimal estimatedPrice;

    private String description;

    private LoadStatus status;

    private Long assignedTruckId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
