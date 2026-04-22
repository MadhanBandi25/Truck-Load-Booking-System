package com.truckbooking.truck_booking.dto.request;

import com.truckbooking.truck_booking.enums.TruckStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTruckStatusRequest {

    @NotNull(message = "Status is required")
    private TruckStatus status;
}
