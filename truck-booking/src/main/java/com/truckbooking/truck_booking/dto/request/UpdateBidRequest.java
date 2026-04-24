package com.truckbooking.truck_booking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBidRequest {

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "1.0", message = "Bid amount must be greater than 0")
    private BigDecimal bidAmount;

    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;
}
