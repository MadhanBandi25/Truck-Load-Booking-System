package com.truckbooking.truck_booking.config;

import com.truckbooking.truck_booking.enums.TruckType;

import java.math.BigDecimal;

public class PriceCalculator {

    public static BigDecimal calculate(TruckType truckType, Double distance , Double weight){
        double pricePerKm;

        switch (truckType) {
            case MINI      -> pricePerKm = 15.0;
            case LCV       -> pricePerKm = 20.0;
            case MCV       -> pricePerKm = 25.0;
            case HCV       -> pricePerKm = 30.0;
            case TRAILER   -> pricePerKm = 40.0;
            case CONTAINER -> pricePerKm = 45.0;
            case TANKER    -> pricePerKm = 50.0;
            case FLATBED   -> pricePerKm = 35.0;
            default        -> pricePerKm = 18.0;
        }

        double weightCharge = 5.0;

        double total = (pricePerKm * distance) + (weight * weightCharge);
        return BigDecimal.valueOf(total);
    }
}
