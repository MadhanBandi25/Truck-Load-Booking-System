package com.truckbooking.truck_booking.service.IMPL;

import com.truckbooking.truck_booking.dto.request.AddTruckRequest;
import com.truckbooking.truck_booking.dto.request.UpdateAvailabilityRequest;
import com.truckbooking.truck_booking.dto.request.UpdateTruckStatusRequest;
import com.truckbooking.truck_booking.dto.response.TruckResponse;
import com.truckbooking.truck_booking.entity.Truck;
import com.truckbooking.truck_booking.entity.User;
import com.truckbooking.truck_booking.enums.Role;
import com.truckbooking.truck_booking.enums.TruckAvailability;
import com.truckbooking.truck_booking.enums.TruckStatus;
import com.truckbooking.truck_booking.enums.TruckType;
import com.truckbooking.truck_booking.exception.DuplicateEntryException;
import com.truckbooking.truck_booking.exception.InvalidStatusException;
import com.truckbooking.truck_booking.exception.ResourceNotFoundException;
import com.truckbooking.truck_booking.exception.UnauthorizedException;
import com.truckbooking.truck_booking.mapper.TruckMapper;
import com.truckbooking.truck_booking.repository.TruckRepository;
import com.truckbooking.truck_booking.repository.UserRepository;
import com.truckbooking.truck_booking.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TruckServiceImpl implements TruckService {

    @Autowired
    private TruckRepository truckRepository;
    @Autowired
    private UserRepository userRepository;

    //  OPERATOR

    @Override
    public TruckResponse addTruck(AddTruckRequest request, Long operatorId) {

        if (truckRepository.existsByTruckNumberIgnoreCase(request.getTruckNumber())){
            throw new DuplicateEntryException("Truck number already exists ");
        }

        User operator = getUser(operatorId);

        if (operator.getRole() != Role.OPERATOR){
            throw new UnauthorizedException("Only operators can add trucks");
        }

        Truck truck = TruckMapper.toEntity(request,operator);
        Truck saved = truckRepository.save(truck);
        return TruckMapper.toResponse(saved);
    }

    @Override
    public List<TruckResponse> getMyTrucks(Long operatorId) {
        return truckRepository.findByOperatorIdAndDeletedAtIsNull(operatorId)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public TruckResponse getMyTruckById(Long truckId, Long operatorId) {
         Truck truck = getTruck(truckId);

         if (!truck.getOperator().getId().equals(operatorId)){
             throw new UnauthorizedException("Access denied not a operator");
         }
         return TruckMapper.toResponse(truck);
    }

    @Override
    public List<TruckResponse> searchMyTrucks(Long operatorId, String keyword) {
        return truckRepository.findByTruckNumberContainingIgnoreCase(keyword)
                .stream()
                .filter(t-> t.getOperator().getId().equals(operatorId) && t.getDeletedAt() == null)
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getMyTrucksByType(Long operatorId, TruckType truckType) {
        return truckRepository.findByTruckType(truckType)
                .stream()
                .filter(t-> t.getOperator().getId().equals(operatorId) && t.getDeletedAt() == null)
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getMyTrucksByStatus(Long operatorId, TruckStatus status) {
        return truckRepository.findByStatus(status)
                .stream()
                .filter(t->t.getOperator().getId().equals(operatorId) && t.getDeletedAt() == null)
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getMyTrucksByAvailability(Long operatorId, TruckAvailability availability) {
        return truckRepository.findByAvailability(availability)
                .stream()
                .filter(t-> t.getOperator().getId().equals(operatorId) && t.getDeletedAt() == null)
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteMyTruck(Long truckId, Long operatorId) {

        Truck truck = getTruck(truckId);
        if (!truck.getOperator().getId().equals(operatorId)){
            throw new UnauthorizedException("You can delete only your trucks");
        }

        if (truck.getDeletedAt() != null) {
            throw new InvalidStatusException("Truck already deleted");
        }

        truck.setDeletedAt(LocalDateTime.now());
        truck.setStatus(TruckStatus.INACTIVE);
        truckRepository.save(truck);
    }

    //  admin

    @Override
    public List<TruckResponse> getAllTrucks() {
        return truckRepository.findAll()
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public TruckResponse getTruckById(Long truckId) {
        Truck truck = getTruck(truckId);
        return TruckMapper.toResponse(truck);
    }

    @Override
    public TruckResponse getByTruckNumber(String truckNumber) {
        Truck truck = truckRepository.findByTruckNumberIgnoreCase(truckNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Truck not found"));
        return TruckMapper.toResponse(truck);
    }

    @Override
    public List<TruckResponse> searchByTruckNumber(String truckNumber) {
        return truckRepository.findByTruckNumberContainingIgnoreCase(truckNumber)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getTrucksByOperator(Long operatorId) {
        User user = getUser(operatorId);

        if (user.getRole() != Role.OPERATOR) {
            throw new UnauthorizedException("User is not an operator");
        }
        return truckRepository.findByOperatorIdAndDeletedAtIsNull(operatorId)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getTrucksByType(TruckType truckType) {
        return truckRepository.findByTruckType(truckType)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getTrucksByStatus(TruckStatus status) {
        return truckRepository.findByStatus(status)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getTrucksByAvailability(TruckAvailability availability) {
        return truckRepository.findByAvailability(availability)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getTrucksByStatusAndAvailability(TruckStatus status, TruckAvailability availability) {
        return truckRepository.findByStatusAndAvailability(status,availability)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public TruckResponse updateTruckStatus(Long id, UpdateTruckStatusRequest request) {

        Truck truck = getTruck(id);
        if (request.getStatus() == null){
            throw new InvalidStatusException("Status cannot be null");
        }

        truck.setStatus(request.getStatus());

        return TruckMapper.toResponse(truckRepository.save(truck));
    }

    @Override
    public void deleteTruck(Long truckId) {

        Truck truck = getTruck(truckId);

        if (truck.getDeletedAt() != null){
            throw new InvalidStatusException("Truck already deleted");
        }

        truck.setDeletedAt(LocalDateTime.now());
        truck.setStatus(TruckStatus.INACTIVE);
        truckRepository.save(truck);

    }

    @Override
    public TruckResponse activateTruck(String truckNumber) {

        Truck truck = truckRepository.findByTruckNumberIgnoreCase(truckNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Truck not found"));

        if (truck.getStatus() == TruckStatus.ACTIVE){
            throw new InvalidStatusException("Truck is already active");
        }

        if (truck.getDeletedAt()!= null){
            truck.setDeletedAt(null);
        }

        truck.setStatus(TruckStatus.ACTIVE);

        Truck saved = truckRepository.save(truck);
        return TruckMapper.toResponse(saved);
    }

    // system do automatically
    @Override
    public List<TruckResponse> getAvailableTrucks() {
        return truckRepository.
                findByStatusAndAvailability(TruckStatus.ACTIVE, TruckAvailability.AVAILABLE)
                .stream()
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public List<TruckResponse> getAvailableTrucksByType(TruckType truckType) {
        return truckRepository.findByTruckType(truckType)
                .stream()
                .filter(t-> t.getStatus() == TruckStatus.ACTIVE &&
                        t.getAvailability() == TruckAvailability.AVAILABLE &&
                        t.getDeletedAt() == null)
                .map(TruckMapper::toResponse)
                .toList();
    }

    @Override
    public TruckResponse updateTruckAvailability(Long id, UpdateAvailabilityRequest request) {

        Truck truck = getTruck(id);
        if (request.getAvailability() == null){
            throw new IllegalArgumentException("Availability cannot be null");
        }

        truck.setAvailability(request.getAvailability());
        return TruckMapper.toResponse(truckRepository.save(truck));
    }

    private Truck getTruck(Long id){
        return truckRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Truck not found"));
    }

    private User getUser(Long id){
        return  userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found"));
    }
}
