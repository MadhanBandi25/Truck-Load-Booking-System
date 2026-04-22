package com.truckbooking.truck_booking.dto.request;

import com.truckbooking.truck_booking.enums.TruckAvailability;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAvailabilityRequest {

    @NotNull(message = "Availability is required")
    private TruckAvailability availability;
}
