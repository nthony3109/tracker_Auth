package com.expenseTracker.tracker.auth.Service;

import com.expenseTracker.tracker.auth.Model.Rtoken;
import com.expenseTracker.tracker.auth.Model.UserField;
import com.expenseTracker.tracker.auth.repo.RtokenRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RtokenService {
    private  RtokenRepo rtokenRepo;
    public RtokenService (RtokenRepo repo) {
        this.rtokenRepo = repo;
    }
    public Rtoken generateRtoken(UserField user) {
        Rtoken rtoken = new Rtoken();
        rtoken.setRefreshToken(UUID.randomUUID().toString());
        rtoken.setExpiryDate(LocalDateTime.now().plusDays(30));
        rtoken.setUserField(user);
        return rtokenRepo.save(rtoken);
    }
    public  boolean isTokenValid(String token) {
        return  rtokenRepo.findByRtoken(token)
                .filter(tk -> tk.getExpiryDate()
                        .isAfter(LocalDateTime.now()))
                .isPresent();
    }
    public void deleteRtoken(UserField  user) {
        rtokenRepo.deleteAllRtokenByUser(user);
    }

}
