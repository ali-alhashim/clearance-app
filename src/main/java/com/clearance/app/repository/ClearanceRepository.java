package com.clearance.app.repository;

import com.clearance.app.model.Clearance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClearanceRepository extends MongoRepository<Clearance, String> {

    List<Clearance> findByCreatedByUserId(String userId);

    List<Clearance> findByBadgeNumber(String employeeId);
}
