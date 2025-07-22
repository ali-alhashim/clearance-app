package com.clearance.app.repository;

import com.clearance.app.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AppUserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);

    @Query("{ '$or': [ " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'email': { $regex: ?0, $options: 'i' } }, " +
            "  { 'department': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<AppUser> findByKeyword(String keyword, Pageable pageable);



}
