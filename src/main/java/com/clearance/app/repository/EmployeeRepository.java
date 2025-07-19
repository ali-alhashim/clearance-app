package com.clearance.app.repository;

import com.clearance.app.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Optional<Employee> findByBadgeNumber(String badgeNumber);
    Optional<Employee> findByEmail(String email);
}
