package com.truckbooking.truck_booking.entity;

import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trucks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;

    @Column(nullable = false, unique = true)
    private String truckNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckType truckType;

    @Column(nullable = false)
    private Double capacityTons;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private Integer manufactureYear;

    private String  rcDocument;
    private String permitDocument;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckStatus status = TruckStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckAvailability availability =TruckAvailability.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
