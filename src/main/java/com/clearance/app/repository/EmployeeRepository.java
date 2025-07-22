package com.clearance.app.repository;


import com.clearance.app.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Optional<Employee> findByBadgeNumber(String badgeNumber);
    Optional<Employee> findByEmail(String email);



    @Query("{ '$or': [ " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'email': { $regex: ?0, $options: 'i' } }, " +
            "  { 'badgeNumber': { $regex: ?0, $options: 'i' } }, " +
            "  { 'department': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<Employee> findByKeyword(String keyword, Pageable pageable);
}
