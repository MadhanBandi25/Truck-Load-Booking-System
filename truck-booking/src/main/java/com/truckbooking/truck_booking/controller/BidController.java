package com.truckbooking.truck_booking.controller;

import com.truckbooking.truck_booking.dto.request.BidRequest;
import com.truckbooking.truck_booking.dto.request.UpdateBidRequest;
import com.truckbooking.truck_booking.dto.response.BidResponse;
import com.truckbooking.truck_booking.enums.BidStatus;
import com.truckbooking.truck_booking.service.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping("/operator/{operatorId}")
    public ResponseEntity<BidResponse> placeBid(@PathVariable Long operatorId, @Valid @RequestBody BidRequest request) {
        BidResponse response = bidService.placeBid(request, operatorId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/operator/{operatorId}/{bidId}")
    public ResponseEntity<BidResponse> updateBid(@PathVariable Long operatorId, @PathVariable Long bidId,
            @Valid @RequestBody UpdateBidRequest request) {
        return ResponseEntity.ok(bidService.updateBid(bidId, request, operatorId));
    }

    @PatchMapping("/operator/{operatorId}/{bidId}/withdraw")
    public ResponseEntity<String> withdrawBid(@PathVariable Long operatorId, @PathVariable Long bidId) {
        bidService.withdrawBid(bidId, operatorId);
        return ResponseEntity.ok("Bid withdrawn successfully");
    }

    @GetMapping("/operator/{operatorId}")
    public ResponseEntity<List<BidResponse>> getMyBids(@PathVariable Long operatorId) {
        return ResponseEntity.ok(bidService.getMyBids(operatorId));
    }

    @GetMapping("/operator/{operatorId}/{bidId}")
    public ResponseEntity<BidResponse> getMyBidById(@PathVariable Long operatorId, @PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.getMyBidById(bidId, operatorId));
    }

    @GetMapping("/operator/{operatorId}/status")
    public ResponseEntity<List<BidResponse>> getMyBidsByStatus(@PathVariable Long operatorId, @RequestParam BidStatus status) {
        return ResponseEntity.ok(bidService.getMyBidsByStatus(operatorId, status));
    }


    // shipper
    @GetMapping("/shipper/{shipperId}/load/{loadId}")
    public ResponseEntity<List<BidResponse>> getBidsOnMyLoad(@PathVariable Long shipperId, @PathVariable Long loadId) {
        return ResponseEntity.ok(bidService.getBidsOnMyLoad(loadId, shipperId));
    }

    @GetMapping("/shipper/{shipperId}/load/{loadId}/status")
    public ResponseEntity<List<BidResponse>> getBidsOnMyLoadByStatus(@PathVariable Long shipperId, @PathVariable Long loadId, @RequestParam BidStatus status) {
        return ResponseEntity.ok(bidService.getBidsOnMyLoadByStatus(loadId, shipperId, status));
    }


    @PatchMapping("/shipper/{shipperId}/accept/{bidId}")
    public ResponseEntity<BidResponse> acceptBid(@PathVariable Long shipperId, @PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.acceptBid(bidId, shipperId));
    }

    @PatchMapping("/shipper/{shipperId}/reject/{bidId}")
    public ResponseEntity<BidResponse> rejectBid(@PathVariable Long shipperId, @PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.rejectBid(bidId, shipperId));
    }
    // admin
    @GetMapping("/admin/all")
    public ResponseEntity<List<BidResponse>> getAllBids() {
        return ResponseEntity.ok(bidService.getAllBids());
    }

    @GetMapping("/admin/{bidId}")
    public ResponseEntity<BidResponse> getBidById(@PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.getBidById(bidId));
    }

    @GetMapping("/admin/status")
    public ResponseEntity<List<BidResponse>> getBidsByStatus(@RequestParam BidStatus status) {
        return ResponseEntity.ok(bidService.getBidsByStatus(status));
    }

    @GetMapping("/admin/load/{loadId}")
    public ResponseEntity<List<BidResponse>> getBidsByLoad(@PathVariable Long loadId) {
        return ResponseEntity.ok(bidService.getBidsByLoad(loadId));
    }

    @GetMapping("/admin/operator/{operatorId}")
    public ResponseEntity<List<BidResponse>> getBidsByOperator(@PathVariable Long operatorId) {
        return ResponseEntity.ok(bidService.getBidsByOperator(operatorId));
    }

    @DeleteMapping("/admin/{bidId}")
    public ResponseEntity<String> deleteBid(@PathVariable Long bidId) {
        bidService.adminDeleteBid(bidId);
        return ResponseEntity.ok("Bid deleted successfully");
    }
}
