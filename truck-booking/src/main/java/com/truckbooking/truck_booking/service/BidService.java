package com.truckbooking.truck_booking.service;

import com.truckbooking.truck_booking.dto.request.BidRequest;
import com.truckbooking.truck_booking.dto.request.UpdateBidRequest;
import com.truckbooking.truck_booking.dto.response.BidResponse;
import com.truckbooking.truck_booking.enums.BidStatus;

import java.util.List;

public interface BidService {

    // operator
    BidResponse placeBid(BidRequest request, Long operatorId);
    BidResponse updateBid(Long bidId, UpdateBidRequest request, Long operatorId);
    void withdrawBid(Long bidId, Long operatorId);
    List<BidResponse> getMyBids(Long operatorId);
    List<BidResponse> getMyBidsByStatus(Long operatorId, BidStatus status);
    BidResponse getMyBidById(Long bidId, Long operatorId);

    // ── Shipper
    List<BidResponse> getBidsOnMyLoad(Long loadId, Long shipperId);
    List<BidResponse> getBidsOnMyLoadByStatus(Long loadId, Long shipperId, BidStatus status);
    BidResponse acceptBid(Long bidId, Long shipperId);
    BidResponse rejectBid(Long bidId, Long shipperId);

    // admin
    List<BidResponse> getAllBids();
    BidResponse getBidById(Long bidId);
    List<BidResponse> getBidsByStatus(BidStatus status);
    List<BidResponse> getBidsByLoad(Long loadId);
    List<BidResponse> getBidsByOperator(Long operatorId);
    void adminDeleteBid(Long bidId);

}
