package com.truckbooking.truck_booking.mapper;

import com.truckbooking.truck_booking.dto.request.BidRequest;
import com.truckbooking.truck_booking.dto.request.LoadRequest;
import com.truckbooking.truck_booking.dto.request.UpdateBidRequest;
import com.truckbooking.truck_booking.dto.response.BidResponse;
import com.truckbooking.truck_booking.entity.Bid;
import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.entity.Truck;
import com.truckbooking.truck_booking.entity.User;
import com.truckbooking.truck_booking.enums.BidStatus;

import java.util.List;

public class BidMapper {

    public static Bid toEntity(BidRequest request, Load load, User operator){

        Bid bid = new Bid();

        bid.setLoad(load);
        bid.setOperator(operator);
        bid.setBidAmount(request.getBidAmount());
        bid.setMessage(request.getMessage());

        return bid;
    }

    public static BidResponse toResponse(Bid bid){

        BidResponse response = new BidResponse();

        response.setId(bid.getId());

        if (bid.getLoad() != null){
            response.setLoadId(bid.getLoad().getId());
            response.setPickupLocation(bid.getLoad().getPickupLocation());
            response.setDropLocation(bid.getLoad().getDropLocation());
            response.setEstimatedPrice(bid.getLoad().getEstimatedPrice());
        }

        if (bid.getOperator() != null) {
            response.setOperatorId(bid.getOperator().getId());
            response.setOperatorName(bid.getOperator().getName());
            response.setOperatorPhone(bid.getOperator().getPhone());
        }

        if (bid.getLoad() != null && bid.getLoad().getAssignedTruck() != null) {

            Truck truck = bid.getLoad().getAssignedTruck();

            response.setTruckId(truck.getId());
            response.setTruckNumber(truck.getTruckNumber());
            response.setTruckType(truck.getTruckType());
        }

        response.setBidAmount(bid.getBidAmount());
        response.setMessage(bid.getMessage());
        response.setStatus(bid.getStatus());

        response.setCreatedAt(bid.getCreatedAt());
        response.setUpdatedAt(bid.getUpdatedAt());
        response.setDeletedAt(bid.getDeletedAt());

        return response;

    }

    public static void updateBidEntity(Bid bid, UpdateBidRequest request){
        bid.setBidAmount(request.getBidAmount());
        bid.setMessage(request.getMessage());
    }

    public static void acceptBid(Bid selectedBid, List<Bid> otherBids){
        selectedBid.setStatus(BidStatus.ACCEPTED);
        if (otherBids != null){
            otherBids.forEach(b-> b.setStatus(BidStatus.REJECTED));
        }
    }
}
