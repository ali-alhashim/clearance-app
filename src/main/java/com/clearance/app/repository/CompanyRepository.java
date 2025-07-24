package com.clearance.app.repository;

import com.clearance.app.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Company findFirstBy();
}
