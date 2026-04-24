package com.truckbooking.truck_booking.repository;

import com.truckbooking.truck_booking.entity.Bid;
import com.truckbooking.truck_booking.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Long> {

    Optional<Bid> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByLoadIdAndOperatorIdAndDeletedAtIsNull(Long loadId, Long operatorId);

    // operator
    List<Bid> findByOperatorIdAndDeletedAtIsNull(Long operatorId);
    List<Bid> findByOperatorIdAndStatusAndDeletedAtIsNull(Long operatorId, BidStatus status);

    // load
    List<Bid> findByLoadIdAndDeletedAtIsNull(Long loadId);
    List<Bid> findByLoadIdAndStatusAndDeletedAtIsNull(Long loadId, BidStatus status);

    // accept logic
    Optional<Bid> findFirstByLoadIdAndStatusAndDeletedAtIsNull(Long loadId, BidStatus status);

    List<Bid> findByLoadIdAndStatusAndIdNotAndDeletedAtIsNull(
            Long loadId,
            BidStatus status,
            Long bidId
    );
    // admin
    List<Bid> findByDeletedAtIsNull();
}
