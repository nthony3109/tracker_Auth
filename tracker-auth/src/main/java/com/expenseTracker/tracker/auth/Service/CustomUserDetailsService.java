package com.expenseTracker.tracker.auth.Service;


import com.expenseTracker.tracker.auth.Model.UserField;
import com.expenseTracker.tracker.auth.repo.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private  final UserRepo repo;

    public CustomUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserField appUser = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found" + username));
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles("user")
                .build();
    }
}
