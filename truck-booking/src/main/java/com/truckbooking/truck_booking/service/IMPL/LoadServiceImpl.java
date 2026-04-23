package com.truckbooking.truck_booking.service.IMPL;

import com.truckbooking.truck_booking.config.PriceCalculator;
import com.truckbooking.truck_booking.dto.request.AssignTruckRequest;
import com.truckbooking.truck_booking.dto.request.LoadRequest;
import com.truckbooking.truck_booking.dto.request.UpdateLoadStatusRequest;
import com.truckbooking.truck_booking.dto.response.LoadResponse;
import com.truckbooking.truck_booking.entity.Load;
import com.truckbooking.truck_booking.entity.Truck;
import com.truckbooking.truck_booking.entity.User;
import com.truckbooking.truck_booking.enums.*;
import com.truckbooking.truck_booking.exception.DuplicateEntryException;
import com.truckbooking.truck_booking.exception.InvalidStatusException;
import com.truckbooking.truck_booking.exception.ResourceNotFoundException;
import com.truckbooking.truck_booking.exception.UnauthorizedException;
import com.truckbooking.truck_booking.mapper.LoadMapper;
import com.truckbooking.truck_booking.repository.LoadRepository;
import com.truckbooking.truck_booking.repository.TruckRepository;
import com.truckbooking.truck_booking.repository.UserRepository;
import com.truckbooking.truck_booking.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoadServiceImpl implements LoadService {

    @Autowired
    private LoadRepository loadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TruckRepository truckRepository;

    // shipper
    @Override
    public LoadResponse createLoad(LoadRequest request, Long shipperId) {

        LocalDateTime pickupDateTime = LocalDateTime.of(
                request.getPickupDate(),
                request.getPickupTime()
        );

        if (pickupDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidStatusException("Pickup date & time must be in future");
        }
        User shipper = getUser(shipperId);
        if (shipper.getRole() != Role.SHIPPER){
            throw new UnauthorizedException("Only shipper can create load");
        }

        if (loadRepository.existsByShipperIdAndPickupLocationIgnoreCaseAndDropLocationIgnoreCaseAndPickupDateAndPickupTimeAndWeightAndTruckTypeAndDeletedAtIsNull(
                shipperId,
                request.getPickupLocation(), request.getDropLocation(),
                request.getPickupDate(), request.getPickupTime(),
                request.getWeight(), request.getTruckType())){
            throw new DuplicateEntryException("Duplicate load");
        }

        var price = PriceCalculator.calculate(request.getTruckType(), request.getDistance(), request.getWeight());

        Load load = LoadMapper.toEntity(request,shipper,price);

        return LoadMapper.toResponse(loadRepository.save(load));
    }

    @Override
    public List<LoadResponse> getMyLoads(Long shipperId) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public LoadResponse getMyLoadById(Long loadId, Long shipperId) {
        Load load = loadRepository.findByIdAndShipperIdAndDeletedAtIsNull(loadId,shipperId)
                .orElseThrow(()-> new ResourceNotFoundException("Load not found"));

        return LoadMapper.toResponse(load);
    }

    @Override
    public List<LoadResponse> getMyLoadsByStatus(Long shipperId, LoadStatus status) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .filter(l-> l.getStatus() == status)
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getMyLoadsByCargoType(Long shipperId, CargoType cargoType) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .filter(l-> l.getCargoType() == cargoType)
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getMyLoadsByTruckType(Long shipperId, TruckType truckType) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .filter(l-> l.getTruckType() == truckType)
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getMyLoadsByDate(Long shipperId, LocalDate date) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .filter(l-> l.getPickupDate().equals(date))
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> searchMyLoadsByPickup(Long shipperId, String keyword) {
        return loadRepository.findByPickupLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .filter(l-> l.getShipper().getId().equals(shipperId))
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> searchMyLoadsByDrop(Long shipperId, String keyword) {
        return loadRepository.findByDropLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .filter(l-> l.getShipper().getId().equals(shipperId))
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public void cancelMyLoad(Long loadId, Long shipperId) {

        Load load = loadRepository
                .findByIdAndShipperIdAndDeletedAtIsNull(loadId, shipperId)
                .orElseThrow(() -> new RuntimeException("Load not found"));



        if (load.getStatus() == LoadStatus.DELIVERED || load.getStatus() == LoadStatus.COMPLETED) {
            throw new InvalidStatusException("Cannot cancel completed load");
        }
        load.setStatus(LoadStatus.CANCELLED);
        loadRepository.save(load);

    }

    // operator

    @Override
    public List<LoadResponse> getOpenLoads() {
        return loadRepository.findByStatusAndAssignedTruckIsNullAndDeletedAtIsNull(LoadStatus.OPEN)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getOpenLoadsByTruckType(TruckType truckType) {
        return  loadRepository
                .findByStatusAndTruckTypeAndDeletedAtIsNull(LoadStatus.OPEN, truckType)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getOpenLoadsByCargoType(CargoType cargoType) {
        return  loadRepository
                .findByStatusAndCargoTypeAndAssignedTruckIsNullAndDeletedAtIsNull(
                        LoadStatus.OPEN, cargoType)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getOpenLoadsByDate(LocalDate date) {
        return getOpenLoads()
                .stream()
                .filter(l-> l.getPickupDate().equals(date))
                .toList();
    }

    @Override
    public List<LoadResponse> searchOpenLoadsByPickup(String keyword) {
        return loadRepository.findByPickupLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .filter(l-> l.getStatus() == LoadStatus.OPEN && l.getAssignedTruck() == null)
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> searchOpenLoadsByDrop(String keyword) {
        return loadRepository.findByDropLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .filter(l-> l.getStatus() == LoadStatus.OPEN && l.getAssignedTruck() == null)
                .map(LoadMapper::toResponse)
                .toList();
    }

    // assign logic

    private LoadResponse assignInternal(Long loadId, Long truckId){

        Load load = getLoad(loadId);
        Truck truck =getTruck(truckId);

        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidStatusException("Load is not open for assignment");
        }

        if (truck.getAvailability() != TruckAvailability.AVAILABLE){
            throw new InvalidStatusException("Truck not available");
        }
        if (truck.getStatus() != TruckStatus.ACTIVE) {
            throw new InvalidStatusException("Truck is not active");
        }

        if (!isCompatible(load.getTruckType(), truck.getTruckType())) {
            throw new InvalidStatusException("Truck not compatible with load");
        }

        load.setAssignedTruck(truck);
        load.setStatus(LoadStatus.BOOKED);

        truck.setAvailability(TruckAvailability.BOOKED);
        return LoadMapper.toResponse(loadRepository.save(load));
    }

    @Override
    public LoadResponse assignOpenTruck(Long loadId, Long truckId, Long operatorId) {

        User operator = getUser(operatorId);
        if (operator.getRole() != Role.OPERATOR){
            throw new UnauthorizedException("Only operator are assign");
        }
        return assignInternal(loadId, truckId);
    }

    @Override
    public LoadResponse unassignTruck(Long loadId, Long operatorId) {

        User operator = getUser(operatorId);
        if (operator.getRole() != Role.OPERATOR){
            throw new UnauthorizedException("Only operator are unassign");
        }
        Load load = getLoad(loadId);

        if (load.getAssignedTruck() == null){
            throw new InvalidStatusException("No truck assigned");
        }

        Truck truck = load.getAssignedTruck();

        load.setAssignedTruck(null);
        load.setStatus(LoadStatus.OPEN);

        truck.setAvailability(TruckAvailability.AVAILABLE);
        return LoadMapper.toResponse(loadRepository.save(load));
    }

    @Override
    public LoadResponse updateLoadStatusByOperator(Long loadId, UpdateLoadStatusRequest request, Long operatorId) {

        User operator = getUser(operatorId);
        if (operator.getRole() != Role.OPERATOR){
            throw new UnauthorizedException("Only operator are unassign");
        }
        Load load = getLoad(loadId);
        load.setStatus(request.getStatus());
        return LoadMapper.toResponse(loadRepository.save(load));
    }

    // admin
    @Override
    public List<LoadResponse> getAllLoads() {
        return loadRepository.findByDeletedAtIsNull()
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public LoadResponse getLoadById(Long loadId) {
        return LoadMapper.toResponse(getLoad(loadId));
    }

    @Override
    public List<LoadResponse> getAllAssignedLoads() {
        return  loadRepository.findByAssignedTruckIsNotNullAndDeletedAtIsNull()
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByStatus(LoadStatus status) {
        return loadRepository.findByStatusAndDeletedAtIsNull(status)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByCargoType(CargoType cargoType) {
        return loadRepository.findByCargoTypeAndDeletedAtIsNull(cargoType)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByTruckType(TruckType truckType) {
        return loadRepository.findByTruckTypeAndDeletedAtIsNull(truckType)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByDate(LocalDate date) {
        return loadRepository.findByPickupDateAndDeletedAtIsNull(date)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByShipper(Long shipperId) {
        return loadRepository.findByShipperIdAndDeletedAtIsNull(shipperId)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> getLoadsByTruck(Long truckId) {
        return loadRepository.findByAssignedTruckIdAndDeletedAtIsNull(truckId)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> searchByPickupLocation(String keyword) {
        return loadRepository.findByPickupLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public List<LoadResponse> searchByDropLocation(String keyword) {
        return loadRepository.findByDropLocationContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(LoadMapper::toResponse)
                .toList();
    }

    @Override
    public LoadResponse updateLoadStatusByAdmin(Long loadId, UpdateLoadStatusRequest request) {
        Load load = getLoad(loadId);
        load.setStatus(request.getStatus());
        return LoadMapper.toResponse(loadRepository.save(load));
    }

    @Override
    public LoadResponse assignTruck(Long loadId, AssignTruckRequest request) {
        return assignInternal(loadId, request.getTruckId());
    }

    @Override
    public void deleteLoad(Long loadId) {
        Load load = getLoad(loadId);
        if (load.getDeletedAt() != null) {
            throw new InvalidStatusException("Load already deleted");
        }

        load.setDeletedAt(LocalDateTime.now());
        loadRepository.save(load);

    }

    private Load getLoad(Long id){
       return  loadRepository.findByIdAndDeletedAtIsNull(id)
               .orElseThrow(() -> new RuntimeException("Load not found"));
    }
    private User getUser(Long id){
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    private Truck getTruck(Long id) {
        return truckRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
    }

    private boolean isCompatible(TruckType loadType, TruckType truckType) {

        return switch (loadType) {

            case MINI -> truckType == TruckType.MINI || truckType == TruckType.LCV
                       || truckType == TruckType.MCV || truckType == TruckType.HCV;

            case LCV -> truckType == TruckType.LCV || truckType == TruckType.MCV
                        || truckType == TruckType.HCV;

            case MCV -> truckType == TruckType.MCV || truckType == TruckType.HCV;

            case HCV -> truckType == TruckType.HCV || truckType == TruckType.TRAILER;

            case TRAILER -> truckType == TruckType.TRAILER;

            case CONTAINER -> truckType == TruckType.CONTAINER;

            case TANKER -> truckType == TruckType.TANKER;

            case FLATBED -> truckType == TruckType.FLATBED;

            default -> false;
        };
    }
}
