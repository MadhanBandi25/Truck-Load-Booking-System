package com.truckbooking.truck_booking.dto.response;

import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckResponse {

    private Long id;
    private Long operatorId;
    private String operatorName;
    private String operatorPhone;
    private String truckNumber;
    private TruckType truckType;
    private Double capacityTons;
    private String modelName;
    private Integer manufactureYear;
    private String rcDocument;
    private String permitDocument;
    private TruckStatus status;
    private TruckAvailability availability;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
