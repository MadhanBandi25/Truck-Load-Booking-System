package com.truckbooking.truck_booking.mapper;

import com.truckbooking.truck_booking.dto.request.AddTruckRequest;
import com.truckbooking.truck_booking.dto.response.TruckResponse;
import com.truckbooking.truck_booking.entity.Truck;
import com.truckbooking.truck_booking.entity.User;

public class TruckMapper {

    public static Truck toEntity(AddTruckRequest request, User operator){

        Truck truck = new Truck();

        truck.setOperator(operator);
        truck.setTruckNumber(request.getTruckNumber().toUpperCase().trim());
        truck.setTruckType(request.getTruckType());
        truck.setCapacityTons(request.getCapacityTons());
        truck.setModelName(request.getModelName());
        truck.setManufactureYear(request.getManufactureYear());

        return truck;

    }

    public static TruckResponse toResponse(Truck truck){

        TruckResponse response = new TruckResponse();

        response.setId(truck.getId());

        response.setOperatorName(truck.getOperator().getName());
        response.setOperatorPhone(truck.getOperator().getPhone());
        response.setOperatorId(truck.getOperator().getId());

        response.setTruckNumber(truck.getTruckNumber());
        response.setTruckType(truck.getTruckType());
        response.setCapacityTons(truck.getCapacityTons());
        response.setModelName(truck.getModelName());
        response.setManufactureYear(truck.getManufactureYear());

        response.setRcDocument(truck.getRcDocument());
        response.setPermitDocument(truck.getPermitDocument());

        response.setStatus(truck.getStatus());
        response.setAvailability(truck.getAvailability());

        response.setCreatedAt(truck.getCreatedAt());
        response.setUpdatedAt(truck.getUpdatedAt());
        response.setDeletedAt(truck.getDeletedAt());

        return response;
    }
}
