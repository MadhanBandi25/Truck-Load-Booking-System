package com.truckbooking.truck_booking.dto.request;

import com.truckbooking.truck_booking.enums.TruckType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTruckRequest {

    @NotBlank(message = "Truck number is required")
    @Pattern(regexp = "^[A-Za-z]{2}[0-9]{2}[A-Za-z]{1,2}[0-9]{4}$",
            message = "Enter a valid truck number (e.g. AP39SG3843)")
    private String truckNumber;

    @NotNull(message = "Truck type is required")
    private TruckType truckType;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 ton")
    @Max(value = 100, message = "Capacity cannot exceed 100 tons")
    private Double capacityTons;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotNull(message = "Manufacture year is required")
    @Min(value = 1990, message = "Manufacture year must be 1990 or later")
    @Max(value = 2026, message = "Manufacture year cannot be in the future")
    private Integer manufactureYear;


}
