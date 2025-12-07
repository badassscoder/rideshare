package org.example.rideshare.repository;

import org.example.rideshare.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RideRepository extends MongoRepository<Ride, String> {

    // Find all rides with a specific status (e.g. REQUESTED)
    List<Ride> findByStatus(String status);

    // Find all rides for a given user
    List<Ride> findByUserId(String userId);
}
