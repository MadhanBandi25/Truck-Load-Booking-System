package com.truckbooking.truck_booking.mapper;


import com.truckbooking.truck_booking.dto.request.LoadRequest;
import com.truckbooking.truck_booking.dto.response.LoadResponse;
import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.entity.User;

import java.math.BigDecimal;

public class LoadMapper {

    public static Load toEntity(LoadRequest request, User shipper, BigDecimal price){
        Load load = new Load();

        load.setShipper(shipper);
        load.setPickupLocation(request.getPickupLocation().trim());
        load.setDropLocation(request.getDropLocation().trim());

        load.setCargoType(request.getCargoType());
        load.setWeight(request.getWeight());
        load.setDistance(request.getDistance());
        load.setTruckType(request.getTruckType());

        load.setPickupDate(request.getPickupDate());
        load.setPickupTime(request.getPickupTime());

        load.setDescription(request.getDescription());

        load.setEstimatedPrice(price);

        return load;
    }

    public  static LoadResponse toResponse(Load load){

        LoadResponse response = new LoadResponse();

        response.setId(load.getId());
        response.setShipperId(load.getShipper().getId());

        response.setPickupLocation(load.getPickupLocation().trim());
        response.setDropLocation(load.getDropLocation().trim());

        response.setCargoType(load.getCargoType());
        response.setWeight(load.getWeight());
        response.setDistance(load.getDistance());
        response.setTruckType(load.getTruckType());

        response.setPickupDate(load.getPickupDate());
        response.setPickupTime(load.getPickupTime());

        response.setEstimatedPrice(load.getEstimatedPrice());
        response.setDescription(load.getDescription());
        response.setStatus(load.getStatus());

        response.setAssignedTruckId(load.getAssignedTruck() !=null ? load.getAssignedTruck().getId() : null);

        response.setCreatedAt(load.getCreatedAt());
        response.setUpdatedAt(load.getUpdatedAt());
        response.setDeletedAt(load.getDeletedAt());

        return response;

    }

}
