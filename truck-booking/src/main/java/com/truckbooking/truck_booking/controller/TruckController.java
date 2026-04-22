package com.truckbooking.truck_booking.controller;


import com.truckbooking.truck_booking.dto.request.AddTruckRequest;
import com.truckbooking.truck_booking.dto.request.UpdateAvailabilityRequest;
import com.truckbooking.truck_booking.dto.request.UpdateTruckStatusRequest;
import com.truckbooking.truck_booking.dto.response.TruckResponse;
import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import com.truckbooking.truck_booking.service.TruckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {


    @Autowired
    private TruckService truckService;

    // operator

    @PostMapping("/operator/{operatorId}")
    public ResponseEntity<TruckResponse> addTruck(@Valid @RequestBody AddTruckRequest request, @PathVariable Long operatorId){
        TruckResponse response = truckService.addTruck(request, operatorId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // admin

    @GetMapping("/admin/all")
    public ResponseEntity<List<TruckResponse>> getAllTrucks() {
        return ResponseEntity.ok(truckService.getAllTrucks());
    }

    @GetMapping("/admin/id/{truckId}")
    public ResponseEntity<TruckResponse> getTruckById(@PathVariable Long truckId) {
        return ResponseEntity.ok(truckService.getTruckById(truckId));
    }

    @GetMapping("/admin/number/{truckNumber}")
    public ResponseEntity<TruckResponse> getByTruckNumber(@PathVariable String truckNumber) {
        return ResponseEntity.ok(truckService.getByTruckNumber(truckNumber));
    }

    @GetMapping("/admin/search")
    public ResponseEntity<List<TruckResponse>> searchByTruckNumber(@RequestParam String search) {
        return ResponseEntity.ok(truckService.searchByTruckNumber(search));
    }

    @GetMapping("/admin/operator/{operatorId}")
    public ResponseEntity<List<TruckResponse>> getTrucksByOperator(@PathVariable Long operatorId) {
        return ResponseEntity.ok(truckService.getTrucksByOperator(operatorId));
    }

    @GetMapping("/admin/type")
    public ResponseEntity<List<TruckResponse>> getTrucksByType(@RequestParam TruckType type) {
        return ResponseEntity.ok(truckService.getTrucksByType(type));
    }

    @GetMapping("/admin/status")
    public ResponseEntity<List<TruckResponse>> getTrucksByStatus(@RequestParam TruckStatus status) {
        return ResponseEntity.ok(truckService.getTrucksByStatus(status));
    }

    @GetMapping("/admin/available")
    public ResponseEntity<List<TruckResponse>> getTrucksByAvailability(@RequestParam TruckAvailability available) {
        return ResponseEntity.ok(truckService.getTrucksByAvailability(available));
    }

    @GetMapping("/admin/filter")
    public ResponseEntity<List<TruckResponse>> getTrucksByStatusAndAvailability(@RequestParam TruckStatus status, @RequestParam TruckAvailability available) {
        return ResponseEntity.ok(truckService.getTrucksByStatusAndAvailability(status, available));
    }

    @PutMapping("/admin/{truckId}/status")
    public ResponseEntity<TruckResponse> updateTruckStatus(@PathVariable Long truckId, @Valid @RequestBody UpdateTruckStatusRequest request) {
        return ResponseEntity.ok(truckService.updateTruckStatus(truckId, request));
    }

    @DeleteMapping("/admin/{truckId}")
    public ResponseEntity<String> deleteTruck(@PathVariable Long truckId) {
        truckService.deleteTruck(truckId);
        return ResponseEntity.ok("Truck deleted successfully");
    }

    @PatchMapping("/admin/{truckNumber}/activate")
    public ResponseEntity<String> activateTruck(@PathVariable String truckNumber) {
        truckService.activateTruck(truckNumber);
        return ResponseEntity.ok("Truck activated successfully");
    }


    // operator

    @GetMapping("/operator/{operatorId}")
    public ResponseEntity<List<TruckResponse>> getMyTrucks(@PathVariable Long operatorId){
        return ResponseEntity.ok(truckService.getMyTrucks(operatorId));
    }

    @GetMapping("/operator/{operatorId}/{truckId}")
    public ResponseEntity<TruckResponse> getMyTruckById(@PathVariable Long operatorId, @PathVariable Long truckId){
        return ResponseEntity.ok(truckService.getMyTruckById(truckId,operatorId));
    }

    @GetMapping("/operator/{operatorId}/search")
    public ResponseEntity<List<TruckResponse>> searchMyTrucks(@PathVariable Long operatorId, @RequestParam String search) {
        return ResponseEntity.ok(truckService.searchMyTrucks(operatorId, search));
    }

    @GetMapping("/operator/{operatorId}/type")
    public ResponseEntity<List<TruckResponse>> getMyTrucksByType(@PathVariable Long operatorId, @RequestParam TruckType type) {
        return ResponseEntity.ok(truckService.getMyTrucksByType(operatorId, type));
    }
    @GetMapping("/operator/{operatorId}/status")
    public ResponseEntity<List<TruckResponse>> getMyTrucksByStatus(@PathVariable Long operatorId, @RequestParam TruckStatus status) {
        return ResponseEntity.ok(truckService.getMyTrucksByStatus(operatorId, status));
    }

    @GetMapping("/operator/{operatorId}/available")
    public ResponseEntity<List<TruckResponse>> getMyTrucksByAvailability(@PathVariable Long operatorId, @RequestParam TruckAvailability available) {
        return ResponseEntity.ok(truckService.getMyTrucksByAvailability(operatorId, available));
    }

    @DeleteMapping("/operator/{operatorId}/{truckId}")
    public ResponseEntity<String> deleteMyTruck(@PathVariable Long operatorId, @PathVariable Long truckId) {
        truckService.deleteMyTruck(truckId, operatorId);
        return ResponseEntity.ok("Truck deleted successfully");
    }


    //  system do
    @GetMapping("/available")
    public ResponseEntity<List<TruckResponse>> getAvailableTrucks() {
        return ResponseEntity.ok(truckService.getAvailableTrucks());
    }

    @GetMapping("/available/type")
    public ResponseEntity<List<TruckResponse>> getAvailableTrucksByType(@RequestParam TruckType type) {
        return ResponseEntity.ok(truckService.getAvailableTrucksByType(type));
    }

    @PutMapping("/{truckId}/availability")
    public ResponseEntity<TruckResponse> updateTruckAvailability(@PathVariable Long truckId, @Valid @RequestBody UpdateAvailabilityRequest request) {
        return ResponseEntity.ok(truckService.updateTruckAvailability(truckId, request));
    }
}
