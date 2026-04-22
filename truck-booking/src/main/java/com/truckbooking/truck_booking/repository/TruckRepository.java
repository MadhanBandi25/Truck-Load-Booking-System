package com.truckbooking.truck_booking.repository;

import com.truckbooking.truck_booking.entity.Truck;
import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    boolean existsByTruckNumberIgnoreCase(String truckNumber);

    List<Truck> findByOperatorIdAndDeletedAtIsNull(Long operatorId);
    Optional<Truck> findByTruckNumberIgnoreCase(String truckNumber);
    Optional<Truck> findByIdAndDeletedAtIsNull(Long id);

    List<Truck> findByTruckNumberContainingIgnoreCase(String truckNumber);
    List<Truck> findByStatus(TruckStatus status);
    List<Truck> findByTruckType(TruckType truckType);
    List<Truck> findByAvailability(TruckAvailability availability);
    List<Truck> findByStatusAndAvailability(TruckStatus status, TruckAvailability availability);

}
