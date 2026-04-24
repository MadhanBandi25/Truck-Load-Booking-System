package com.truckbooking.truck_booking.dto.response;

import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.enums.BidStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BidResponse {

    private Long id;
    private Long loadId;
    private String pickupLocation;
    private String dropLocation;

    private Long operatorId;
    private String operatorName;
    private String operatorPhone;
    private BigDecimal estimatedPrice;

    private Long truckId;
    private String truckNumber;
    private TruckType truckType;


    private BigDecimal bidAmount;
    private String message;
    private BidStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
