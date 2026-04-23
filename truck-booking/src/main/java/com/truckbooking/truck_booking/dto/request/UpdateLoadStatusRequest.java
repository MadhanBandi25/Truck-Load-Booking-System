package com.truckbooking.truck_booking.dto.request;

import com.truckbooking.truck_booking.enums.LoadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateLoadStatusRequest {

    @NotNull(message = "Status is required")
    private LoadStatus status;

}
