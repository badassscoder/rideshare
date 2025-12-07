package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: Request a ride  POST /api/v1/rides
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest request,
                                           HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        Ride ride = rideService.requestRide(request, authHeader);
        return ResponseEntity.ok(ride);
    }

    // DRIVER: View pending ride requests  GET /api/v1/driver/rides/requests
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> getPendingRides(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        List<Ride> rides = rideService.getPendingRidesForDriver(authHeader);
        return ResponseEntity.ok(rides);
    }

    // DRIVER: Accept a ride  POST /api/v1/driver/rides/{id}/accept
    @PostMapping("/driver/rides/{id}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable String id,
                                           HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        Ride ride = rideService.acceptRide(id, authHeader);
        return ResponseEntity.ok(ride);
    }

    // USER/DRIVER: Complete ride  POST /api/v1/rides/{id}/complete
    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String id,
                                             HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        Ride ride = rideService.completeRide(id, authHeader);
        return ResponseEntity.ok(ride);
    }

    // USER: Get own rides  GET /api/v1/user/rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getUserRides(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        List<Ride> rides = rideService.getUserRides(authHeader);
        return ResponseEntity.ok(rides);
    }
}

