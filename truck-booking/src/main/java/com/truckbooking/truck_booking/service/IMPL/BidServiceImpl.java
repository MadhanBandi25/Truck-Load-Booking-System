package com.truckbooking.truck_booking.service.IMPL;

import com.truckbooking.truck_booking.dto.request.BidRequest;
import com.truckbooking.truck_booking.dto.request.UpdateBidRequest;
import com.truckbooking.truck_booking.dto.response.BidResponse;
import com.truckbooking.truck_booking.entity.Bid;
import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.entity.User;
import com.truckbooking.truck_booking.enums.BidStatus;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.Role;
import com.truckbooking.truck_booking.exception.InvalidStatusException;
import com.truckbooking.truck_booking.exception.ResourceNotFoundException;
import com.truckbooking.truck_booking.exception.UnauthorizedException;
import com.truckbooking.truck_booking.mapper.BidMapper;
import com.truckbooking.truck_booking.repository.BidRepository;
import com.truckbooking.truck_booking.repository.LoadRepository;
import com.truckbooking.truck_booking.repository.UserRepository;
import com.truckbooking.truck_booking.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoadRepository loadRepository;

    private User getUser(Long id){
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with: "+ id));
    }

    private Load getLoad(Long id) {
        return loadRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with: "+id));
    }

    private Bid getBid(Long id) {
        return bidRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with: "+id));
    }

 // operator

    @Override
    public BidResponse placeBid(BidRequest request, Long operatorId) {

        User user = getUser(operatorId);
        if (user.getRole() != Role.OPERATOR){
            throw new UnauthorizedException("Only operator can place bid");
        }
        Load load =getLoad(request.getLoadId());
        if (load.getStatus() != LoadStatus.OPEN){
            throw new InvalidStatusException("Load not open for bidding");
        }

        if (request.getBidAmount().compareTo(load.getEstimatedPrice()) <= 0) {
            throw new InvalidStatusException("Bid must be greater than estimated price");
        }

        if (bidRepository.existsByLoadIdAndOperatorIdAndDeletedAtIsNull(load.getId(), operatorId)){
            throw new InvalidStatusException("Already placed a bid for this load");
        }

        Bid bid =BidMapper.toEntity(request, load, user);
        return BidMapper.toResponse(bidRepository.save(bid));
    }

    @Override
    public BidResponse updateBid(Long bidId, UpdateBidRequest request, Long operatorId) {

        Bid bid = getBid(bidId);

        if (!bid.getOperator().getId().equals(operatorId)){
            throw new UnauthorizedException("You can update only your bid");
        }
        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusException("Only pending bids can be updated");
        }

        if (request.getBidAmount().compareTo(bid.getLoad().getEstimatedPrice()) <= 0) {
            throw new InvalidStatusException("Bid must be greater than estimated price");
        }

        BidMapper.updateBidEntity(bid, request);
        return BidMapper.toResponse(bidRepository.save(bid));
    }

    @Override
    public void withdrawBid(Long bidId, Long operatorId) {

        Bid bid = getBid(bidId);

        if (!bid.getOperator().getId().equals(operatorId)) {
            throw new UnauthorizedException("You can withdraw only your bid");
        }
        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusException("Only pending bids can be withdrawn");
        }

        bid.setStatus(BidStatus.WITHDRAWN);
        bidRepository.save(bid);
    }

    @Override
    public List<BidResponse> getMyBids(Long operatorId) {
        return bidRepository.findByOperatorIdAndDeletedAtIsNull(operatorId)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public List<BidResponse> getMyBidsByStatus(Long operatorId, BidStatus status) {
        return bidRepository.findByOperatorIdAndStatusAndDeletedAtIsNull(operatorId, status)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public BidResponse getMyBidById(Long bidId, Long operatorId) {
        Bid bid = getBid(bidId);

        if (!bid.getOperator().getId().equals(operatorId)) {
            throw new UnauthorizedException("Not your bid");
        }
        return BidMapper.toResponse(bid);
    }

    // shipper
    @Override
    public List<BidResponse> getBidsOnMyLoad(Long loadId, Long shipperId) {
        Load load = getLoad(loadId);

        if (!load.getShipper().getId().equals(shipperId)) {
            throw new UnauthorizedException("Not your load");
        }

        return bidRepository.findByLoadIdAndDeletedAtIsNull(loadId)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public List<BidResponse> getBidsOnMyLoadByStatus(Long loadId, Long shipperId, BidStatus status) {
        Load load = getLoad(loadId);

        if (!load.getShipper().getId().equals(shipperId)) {
            throw new UnauthorizedException("Not your load");
        }

        return bidRepository.findByLoadIdAndStatusAndDeletedAtIsNull(loadId, status)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public BidResponse acceptBid(Long bidId, Long shipperId) {
        Bid bid = getBid(bidId);
        Load load = bid.getLoad();

        if (!load.getShipper().getId().equals(shipperId)) {
            throw new UnauthorizedException("Not your load");
        }
        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidStatusException("Load already booked");
        }

        // only one accepted bid
        bidRepository.findFirstByLoadIdAndStatusAndDeletedAtIsNull(load.getId(), BidStatus.ACCEPTED)
                .ifPresent(b -> {
            throw new InvalidStatusException("Bid already accepted for this load");});

        List<Bid> others = bidRepository
                .findByLoadIdAndStatusAndIdNotAndDeletedAtIsNull(load.getId(), BidStatus.PENDING, bidId);

        BidMapper.acceptBid(bid, others);
        load.setStatus(LoadStatus.BOOKED);

        bidRepository.saveAll(others);
        bidRepository.save(bid);

        return BidMapper.toResponse(bid);
    }

    @Override
    public BidResponse rejectBid(Long bidId, Long shipperId) {
        Bid bid = getBid(bidId);

        if (!bid.getLoad().getShipper().getId().equals(shipperId)) {
            throw new UnauthorizedException("Not your load");
        }
        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusException("Only pending bids can be rejected");
        }
        bid.setStatus(BidStatus.REJECTED);
        return BidMapper.toResponse(bidRepository.save(bid));
    }

    // admin
    @Override
    public List<BidResponse> getAllBids() {
        return bidRepository.findByDeletedAtIsNull()
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public BidResponse getBidById(Long bidId) {
        return BidMapper.toResponse(getBid(bidId));
    }

    @Override
    public List<BidResponse> getBidsByStatus(BidStatus status) {
        return bidRepository.findByDeletedAtIsNull()
                .stream()
                .filter(b -> b.getStatus() == status)
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public List<BidResponse> getBidsByLoad(Long loadId) {
        return bidRepository.findByLoadIdAndDeletedAtIsNull(loadId)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public List<BidResponse> getBidsByOperator(Long operatorId) {
        return bidRepository.findByOperatorIdAndDeletedAtIsNull(operatorId)
                .stream()
                .map(BidMapper::toResponse)
                .toList();
    }

    @Override
    public void adminDeleteBid(Long bidId) {
        Bid bid = getBid(bidId);

        if (bid.getDeletedAt() != null) {
            throw new InvalidStatusException("Bid already deleted");
        }
        bid.setDeletedAt(LocalDateTime.now());
        bidRepository.save(bid);
    }
}
