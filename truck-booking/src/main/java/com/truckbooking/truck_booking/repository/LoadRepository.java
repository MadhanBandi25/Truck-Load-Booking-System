package com.truckbooking.truck_booking.repository;

import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface LoadRepository extends JpaRepository<Load, Long> {

    boolean existsByShipperIdAndPickupLocationIgnoreCaseAndDropLocationIgnoreCaseAndPickupDateAndPickupTimeAndWeightAndTruckTypeAndDeletedAtIsNull(
            Long shipperId,
            String pickupLocation,
            String dropLocation,
            LocalDate pickupDate,
            LocalTime pickupTime,
            Double weight,
            TruckType truckType
    );
     // basic
     Optional<Load> findByIdAndDeletedAtIsNull(Long id);
     List<Load> findByDeletedAtIsNull();

     //shipper
     List<Load> findByShipperIdAndDeletedAtIsNull(Long shipperId);
     Optional<Load> findByIdAndShipperIdAndDeletedAtIsNull(Long id, Long shipperId);
     List<Load> findByAssignedTruckIsNotNullAndDeletedAtIsNull();

     //filter
     List<Load> findByStatusAndDeletedAtIsNull(LoadStatus status);
     List<Load> findByTruckTypeAndDeletedAtIsNull(TruckType truckType);
     List<Load> findByCargoTypeAndDeletedAtIsNull(CargoType cargoType);

     List<Load> findByStatusAndTruckTypeAndDeletedAtIsNull(LoadStatus status, TruckType truckType);

     // location
     List<Load> findByPickupLocationContainingIgnoreCaseAndDeletedAtIsNull(String pickup);
     List<Load> findByDropLocationContainingIgnoreCaseAndDeletedAtIsNull(String drop);


     // operator
    List<Load> findByStatusAndAssignedTruckIsNullAndDeletedAtIsNull(LoadStatus status);
    List<Load> findByStatusAndCargoTypeAndAssignedTruckIsNullAndDeletedAtIsNull(LoadStatus status, CargoType cargoType);

    //date
    List<Load> findByPickupDateAndDeletedAtIsNull(LocalDate date);

    // loads
    List<Load> findByAssignedTruckIdAndDeletedAtIsNull(Long truckId);
}
