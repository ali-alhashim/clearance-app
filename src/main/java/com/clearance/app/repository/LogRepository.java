package com.clearance.app.repository;


import com.clearance.app.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogRepository extends MongoRepository<Log, String> {

    @Query("{ '$or': [ " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'email': { $regex: ?0, $options: 'i' } }, " +
            "  { 'date': { $regex: ?0, $options: 'i' } }, " +
            "  { 'action': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<Log> findByKeyword(String keyword, Pageable pageable);
}
