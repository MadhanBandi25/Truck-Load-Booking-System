package com.truckbooking.truck_booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTruckRequest {

    @NotNull(message = "Truck ID is required")
    private Long truckId;
}
