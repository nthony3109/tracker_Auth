package com.expenseTracker.tracker.auth.repo;

import com.expenseTracker.tracker.auth.Model.UserField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Long, UserField> {

    Optional<UserField> findById(long id);
    Optional<UserField> findByEmail(String email);
    Optional<UserField> findByUsername(String username);
}
