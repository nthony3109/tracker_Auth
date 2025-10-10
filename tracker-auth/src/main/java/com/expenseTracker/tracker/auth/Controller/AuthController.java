package com.expenseTracker.tracker.auth.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@GetMapping("/api")
public class BaseController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTo regDTO) {
        service.registerUser(regDTO);
        return ResponseEntity.ok("user registered successfully");
    }

}
