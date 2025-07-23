package com.clearance.app.repository;


import com.clearance.app.model.Clearance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClearanceRepository extends MongoRepository<Clearance, String> {

    List<Clearance> findByCreatedByUserEmail(String email);

    List<Clearance> findByBadgeNumber(String employeeId);

    List<Clearance> findByCodeStartingWith(String prefix);


    Optional<Clearance> findByCode(String code);

    @Query("{ '$or': [ " +
            "  { 'badgeNumber': { $regex: ?0, $options: 'i' } }, " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'department': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<Clearance> findByKeyword(String keyword, Pageable pageable);

}
