package com.expenseTracker.tracker.auth.repo;

import com.expenseTracker.tracker.auth.Model.Rtoken;
import com.expenseTracker.tracker.auth.Model.UserField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RtokenRepo extends JpaRepository<Rtoken,Long> {
   Optional<Rtoken> findByRefreshToken(String token);
   void  deleteAllRtokenByUser(UserField user);
}
