package com.truckbooking.truck_booking.service;

import com.truckbooking.truck_booking.dto.request.AssignTruckRequest;
import com.truckbooking.truck_booking.dto.request.LoadRequest;
import com.truckbooking.truck_booking.dto.request.UpdateLoadStatusRequest;
import com.truckbooking.truck_booking.dto.response.LoadResponse;
import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.TruckType;

import java.time.LocalDate;
import java.util.List;

public interface LoadService {
    //shipper
    LoadResponse createLoad(LoadRequest request, Long shipperId);
    List<LoadResponse> getMyLoads(Long shipperId);
    LoadResponse getMyLoadById(Long loadId, Long shipperId);

    List<LoadResponse> getMyLoadsByStatus(Long shipperId, LoadStatus status);
    List<LoadResponse> getMyLoadsByCargoType(Long shipperId, CargoType cargoType);
    List<LoadResponse> getMyLoadsByTruckType(Long shipperId, TruckType truckType);
    List<LoadResponse> getMyLoadsByDate(Long shipperId, LocalDate date);

    List<LoadResponse> searchMyLoadsByPickup(Long shipperId, String keyword);
    List<LoadResponse> searchMyLoadsByDrop(Long shipperId, String keyword);

    void cancelMyLoad(Long loadId, Long shipperId);

    // operator
    List<LoadResponse> getOpenLoads();
    List<LoadResponse> getOpenLoadsByTruckType(TruckType truckType);
    List<LoadResponse> getOpenLoadsByCargoType(CargoType cargoType);
    List<LoadResponse> getOpenLoadsByDate(LocalDate date);

    List<LoadResponse> searchOpenLoadsByPickup(String keyword);
    List<LoadResponse> searchOpenLoadsByDrop(String keyword);

    LoadResponse assignOpenTruck(Long loadId,Long truckId, Long operatorId);
    LoadResponse unassignTruck(Long loadId, Long operatorId);

    LoadResponse updateLoadStatusByOperator(Long loadId, UpdateLoadStatusRequest request, Long operatorId);


    // admin
    List<LoadResponse> getAllLoads();
    LoadResponse getLoadById(Long loadId);
    List<LoadResponse> getAllAssignedLoads();

    List<LoadResponse> getLoadsByStatus(LoadStatus status);
    List<LoadResponse> getLoadsByCargoType(CargoType cargoType);
    List<LoadResponse> getLoadsByTruckType(TruckType truckType);
    List<LoadResponse> getLoadsByDate(LocalDate date);
    List<LoadResponse> getLoadsByShipper(Long shipperId);
    List<LoadResponse> getLoadsByTruck(Long truckId);

    List<LoadResponse> searchByPickupLocation(String keyword);
    List<LoadResponse> searchByDropLocation(String keyword);


    LoadResponse updateLoadStatusByAdmin(Long loadId, UpdateLoadStatusRequest request);
    LoadResponse assignTruck(Long loadId, AssignTruckRequest request);
    void deleteLoad(Long loadId);
}
