package com.truckbooking.truck_booking.controller;

import com.truckbooking.truck_booking.dto.request.AssignTruckRequest;
import com.truckbooking.truck_booking.dto.request.LoadRequest;
import com.truckbooking.truck_booking.dto.request.UpdateLoadStatusRequest;
import com.truckbooking.truck_booking.dto.response.LoadResponse;
import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import com.truckbooking.truck_booking.service.LoadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loads")
public class LoadController {

    @Autowired
    private LoadService loadService;

    @PostMapping("/shipper/{shipperId}")
    public ResponseEntity<LoadResponse> createLoad(@PathVariable Long shipperId,@Valid @RequestBody LoadRequest request){
        LoadResponse response = loadService.createLoad(request,shipperId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/shipper/{shipperId}")
    public ResponseEntity<List<LoadResponse>> getMyLoads(@PathVariable Long shipperId) {
        return ResponseEntity.ok(loadService.getMyLoads(shipperId));
    }

    @GetMapping("/shipper/{shipperId}/{loadId}")
    public ResponseEntity<LoadResponse> getMyLoadById(@PathVariable Long shipperId, @PathVariable Long loadId) {
        return ResponseEntity.ok(loadService.getMyLoadById(loadId, shipperId));
    }
    @GetMapping("/shipper/{shipperId}/status")
    public ResponseEntity<List<LoadResponse>> getMyLoadsByStatus(@PathVariable Long shipperId, @RequestParam LoadStatus status) {
        return ResponseEntity.ok(loadService.getMyLoadsByStatus(shipperId, status));
    }

    @GetMapping("/shipper/{shipperId}/cargo")
    public ResponseEntity<List<LoadResponse>> getMyLoadsByCargo(@PathVariable Long shipperId, @RequestParam CargoType cargo) {
        return ResponseEntity.ok(loadService.getMyLoadsByCargoType(shipperId, cargo));
    }

    @GetMapping("/shipper/{shipperId}/type")
    public ResponseEntity<List<LoadResponse>> getMyLoadsByTruckType(@PathVariable Long shipperId, @RequestParam TruckType truckType) {
        return ResponseEntity.ok(loadService.getMyLoadsByTruckType(shipperId, truckType));
    }
    @GetMapping("/shipper/{shipperId}/date")
    public ResponseEntity<List<LoadResponse>> getMyLoadsByDate(@PathVariable Long shipperId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(loadService.getMyLoadsByDate(shipperId, date));
    }

    @GetMapping("/shipper/{shipperId}/search/pickup")
    public ResponseEntity<List<LoadResponse>> searchMyLoadsByPickup(@PathVariable Long shipperId, @RequestParam String pickup) {
        return ResponseEntity.ok(loadService.searchMyLoadsByPickup(shipperId, pickup));
    }
    @GetMapping("/shipper/{shipperId}/search/drop")
    public ResponseEntity<List<LoadResponse>> searchMyLoadsByDrop(@PathVariable Long shipperId, @RequestParam String drop) {
        return ResponseEntity.ok(loadService.searchMyLoadsByDrop(shipperId, drop));
    }

    @PatchMapping("/shipper/{shipperId}/{loadId}/cancel")
    public ResponseEntity<String> cancelLoad(@PathVariable Long shipperId, @PathVariable Long loadId) {
        loadService.cancelMyLoad(loadId, shipperId);
        return ResponseEntity.ok("Load cancelled successfully");
    }
    //  admin
    @GetMapping("/admin/all")
    public ResponseEntity<List<LoadResponse>> getAllLoads() {
        return ResponseEntity.ok(loadService.getAllLoads());
    }
    @GetMapping("/admin/assigned")
    public ResponseEntity<List<LoadResponse>> getAllAssignedLoads() {
        return ResponseEntity.ok(loadService.getAllAssignedLoads());
    }
    @GetMapping("/admin/load/{loadId}")
    public ResponseEntity<LoadResponse> getLoadById(@PathVariable Long loadId) {
        return ResponseEntity.ok(loadService.getLoadById(loadId));
    }
    @GetMapping("/admin/status")
    public ResponseEntity<List<LoadResponse>> getByStatus(@RequestParam LoadStatus status) {
        return ResponseEntity.ok(loadService.getLoadsByStatus(status));
    }
    @GetMapping("/admin/cargo")
    public ResponseEntity<List<LoadResponse>> getByCargo(@RequestParam CargoType cargo) {
        return ResponseEntity.ok(loadService.getLoadsByCargoType(cargo));
    }
    @GetMapping("/admin/type")
    public ResponseEntity<List<LoadResponse>> getByType(@RequestParam TruckType type) {
        return ResponseEntity.ok(loadService.getLoadsByTruckType(type));
    }

    @GetMapping("/admin/date")
    public ResponseEntity<List<LoadResponse>> getByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(loadService.getLoadsByDate(date));
    }

    @GetMapping("/admin/shipper/{shipperId}")
    public ResponseEntity<List<LoadResponse>> getByShipper(@PathVariable Long shipperId) {
        return ResponseEntity.ok(loadService.getLoadsByShipper(shipperId));
    }
    @GetMapping("/admin/truck/{truckId}")
    public ResponseEntity<List<LoadResponse>> getByTruck(@PathVariable Long truckId) {
        return ResponseEntity.ok(loadService.getLoadsByTruck(truckId));

    }
    @GetMapping("/admin/search/pickup")
    public ResponseEntity<List<LoadResponse>> searchByPickupLocation(@RequestParam String pickup){
        return ResponseEntity.ok(loadService.searchByPickupLocation(pickup));
    }

    @GetMapping("/admin/search/drop")
    public ResponseEntity<List<LoadResponse>> searchByDropLocation(@RequestParam String drop){
        return ResponseEntity.ok(loadService.searchByDropLocation(drop));
    }

    @PatchMapping("/admin/{loadId}/status")
    public ResponseEntity<LoadResponse> updateStatusAdmin(@PathVariable Long loadId, @Valid @RequestBody UpdateLoadStatusRequest request) {
        return ResponseEntity.ok(loadService.updateLoadStatusByAdmin(loadId, request));
    }
    @PatchMapping("/admin/{loadId}/assign")
    public ResponseEntity<LoadResponse> assignTruckAdmin(@PathVariable Long loadId, @Valid @RequestBody AssignTruckRequest request) {
        return ResponseEntity.ok(loadService.assignTruck(loadId, request));
    }

    @DeleteMapping("/admin/{loadId}")
    public ResponseEntity<String> deleteLoad(@PathVariable Long loadId) {
        loadService.deleteLoad(loadId);
        return ResponseEntity.ok("Load deleted successfully");
    }

    // operator

    @GetMapping("/operator/open")
    public ResponseEntity<List<LoadResponse>> getOpenLoads() {
        return ResponseEntity.ok(loadService.getOpenLoads());
    }
    @GetMapping("/operator/open/type")
    public ResponseEntity<List<LoadResponse>> getOpenLoadsByType(@RequestParam TruckType type) {
        return ResponseEntity.ok(loadService.getOpenLoadsByTruckType(type));
    }

    @GetMapping("/operator/open/cargo")
    public ResponseEntity<List<LoadResponse>> getOpenLoadsByCargo(@RequestParam CargoType cargo) {
        return ResponseEntity.ok(loadService.getOpenLoadsByCargoType(cargo));
    }
    @GetMapping("/operator/open/date")
    public ResponseEntity<List<LoadResponse>> getOpenLoadsByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(loadService.getOpenLoadsByDate(date));
    }
    @GetMapping("/operator/open/search/pickup")
    public ResponseEntity<List<LoadResponse>> searchOpenByPickup(@RequestParam String pickup) {
        return ResponseEntity.ok(loadService.searchOpenLoadsByPickup(pickup));
    }
    @GetMapping("/operator/open/search/drop")
    public ResponseEntity<List<LoadResponse>> searchOpenByDrop(@RequestParam String drop) {
        return ResponseEntity.ok(loadService.searchOpenLoadsByDrop(drop));
    }
    @PatchMapping("/operator/{operatorId}/{loadId}/assign")
    public ResponseEntity<LoadResponse> assignTruck(@PathVariable Long operatorId, @PathVariable Long loadId, @RequestParam Long truckId) {
        return ResponseEntity.ok(loadService.assignOpenTruck(loadId, truckId, operatorId));
    }
    @PatchMapping("/operator/{operatorId}/{loadId}/unassign")
    public ResponseEntity<LoadResponse> unassignTruck(@PathVariable Long operatorId, @PathVariable Long loadId) {
        return ResponseEntity.ok(loadService.unassignTruck(loadId, operatorId));
    }

    @PatchMapping("/operator/{operatorId}/{loadId}/status")
    public ResponseEntity<LoadResponse> updateStatusByOperator(@PathVariable Long operatorId, @PathVariable Long loadId, @Valid @RequestBody UpdateLoadStatusRequest request) {
        return ResponseEntity.ok(loadService.updateLoadStatusByOperator(loadId, request, operatorId));
    }
}
