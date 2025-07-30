package com.clearance.app.repository;

import com.clearance.app.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AppUserRepository extends MongoRepository<AppUser, String> {

      @Query("{ 'email': { $regex: '^?0$', $options: 'i' } }")
    Optional<AppUser> findByEmailIgnoreCaseInternal(String email);

    default Optional<AppUser> findByEmailIgnoreCase(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty(); // skip query for empty email
        }
        return findByEmailIgnoreCaseInternal(email);
    }

    List<AppUser> findByRole(String role);

    List<AppUser> findByDepartment(String department);

    List<AppUser> findByIsManager(boolean isManager);

    @Query("{ '$or': [ " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'email': { $regex: ?0, $options: 'i' } }, " +
            "  { 'department': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<AppUser> findByKeyword(String keyword, Pageable pageable);



}
