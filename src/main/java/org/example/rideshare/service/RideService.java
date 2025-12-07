package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RideService(RideRepository rideRepository,
                       UserRepository userRepository,
                       JwtUtil jwtUtil) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 1) Common helper: get User from Authorization header
    private User getUserFromToken(String authHeader) {
        String token = jwtUtil.resolveToken(authHeader);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new BadRequestException("Invalid or missing token");
        }
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    // 2) POST /api/v1/rides  (USER requests a ride)
    public Ride requestRide(CreateRideRequest request, String authHeader) {
        User user = getUserFromToken(authHeader);
        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only USER role can request rides");
        }

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setDriverId(null);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(new Date());

        return rideRepository.save(ride);
    }

    // 3) GET /api/v1/driver/rides/requests  (DRIVER views pending)
    public List<Ride> getPendingRidesForDriver(String authHeader) {
        User driver = getUserFromToken(authHeader);
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only DRIVER role can view pending rides");
        }
        return rideRepository.findByStatus("REQUESTED");
    }

    // 4) POST /api/v1/driver/rides/{id}/accept  (DRIVER accepts)
    public Ride acceptRide(String rideId, String authHeader) {
        User driver = getUserFromToken(authHeader);
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only DRIVER role can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED status");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return rideRepository.save(ride);
    }

    // 5) POST /api/v1/rides/{id}/complete  (USER or DRIVER completes)
    public Ride completeRide(String rideId, String authHeader) {
        User user = getUserFromToken(authHeader); // could be user or driver

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in ACCEPTED status");
        }

        // (Simple version: any logged in user or driver can complete as per assignment)
        ride.setStatus("COMPLETED");

        return rideRepository.save(ride);
    }

    // 6) GET /api/v1/user/rides  (USER sees own rides)
    public List<Ride> getUserRides(String authHeader) {
        User user = getUserFromToken(authHeader);
        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only USER role can view their rides");
        }
        return rideRepository.findByUserId(user.getId());
    }
}
