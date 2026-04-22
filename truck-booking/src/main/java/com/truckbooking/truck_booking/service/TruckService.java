package com.truckbooking.truck_booking.service;

import com.truckbooking.truck_booking.dto.request.AddTruckRequest;
import com.truckbooking.truck_booking.dto.request.UpdateAvailabilityRequest;
import com.truckbooking.truck_booking.dto.request.UpdateTruckStatusRequest;
import com.truckbooking.truck_booking.dto.response.TruckResponse;
import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;

import java.util.List;

public interface TruckService {

    // operator do
    TruckResponse addTruck(AddTruckRequest request, Long operatorId);
    List<TruckResponse> getMyTrucks(Long operatorId);
    TruckResponse getMyTruckById(Long truckId, Long operatorId);
    List<TruckResponse> searchMyTrucks(Long operatorId, String keyword);
    List<TruckResponse> getMyTrucksByType(Long operatorId, TruckType truckType);
    List<TruckResponse> getMyTrucksByStatus(Long operatorId, TruckStatus status);
    List<TruckResponse> getMyTrucksByAvailability(Long operatorId, TruckAvailability availability);
    void deleteMyTruck(Long truckId, Long operatorId);

    //  admin do
    List<TruckResponse>  getAllTrucks();
    TruckResponse getTruckById(Long truckId);
    TruckResponse getByTruckNumber(String truckNumber);
    List<TruckResponse> searchByTruckNumber(String truckNumber);

    List<TruckResponse> getTrucksByOperator(Long operatorId);
    List<TruckResponse> getTrucksByType(TruckType truckType);
    List<TruckResponse> getTrucksByStatus(TruckStatus  status);
    List<TruckResponse> getTrucksByAvailability(TruckAvailability availability);
    List<TruckResponse> getTrucksByStatusAndAvailability(TruckStatus status,TruckAvailability availability);

    TruckResponse updateTruckStatus(Long id, UpdateTruckStatusRequest request);
    void deleteTruck(Long truckId);

    TruckResponse activateTruck(String truckNumber);

    // system do
    List<TruckResponse> getAvailableTrucks();
    List<TruckResponse> getAvailableTrucksByType(TruckType truckType);
    TruckResponse updateTruckAvailability(Long id, UpdateAvailabilityRequest request);

}
