package com.clearance.app.repository;

import com.clearance.app.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailAndOtpCode(String email, String otpCode);

}
