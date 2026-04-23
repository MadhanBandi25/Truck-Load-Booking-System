package com.truckbooking.truck_booking.entity;

import com.truckbooking.truck_booking.enums.CargoType;
import com.truckbooking.truck_booking.enums.LoadStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "loads")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Load {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id", nullable = false)
    private User shipper;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoType cargoType;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double distance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckType truckType;

    @Column(nullable = false)
    private LocalDate pickupDate;
    @Column(nullable = false)
    private LocalTime pickupTime;

    private BigDecimal estimatedPrice;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status =LoadStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck assignedTruck;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
