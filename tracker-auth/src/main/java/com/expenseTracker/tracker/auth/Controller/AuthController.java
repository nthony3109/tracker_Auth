package com.expenseTracker.tracker.auth.Controller;

import com.expenseTracker.tracker.auth.DTO.LoginDTO;
import com.expenseTracker.tracker.auth.DTO.LoginRes;
import com.expenseTracker.tracker.auth.DTO.ProfileDTO;
import com.expenseTracker.tracker.auth.DTO.RegisterDTO;
import com.expenseTracker.tracker.auth.Model.UserField;
import com.expenseTracker.tracker.auth.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
  private  final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }


    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO regDTO) {
      boolean isSaved = service.registerUser(regDTO);
        System.out.println("from controller" + regDTO);

        if (isSaved)  {
        return ResponseEntity.ok("user reistered");
      }
      else
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user not registered");
    }

    @PostMapping("/auth/login")
  public  ResponseEntity<?> loiginUser(@RequestBody LoginDTO loginReq) {
     LoginRes loginRes  = service.loginUser(loginReq);
     // return ResponseEntity.ok(Map.of("token",token));
        return ResponseEntity.ok(loginRes);
    }
    // to get user profile
    @GetMapping("/auth/profile/{userId}")
    public  ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        //String userId = (String) req.getAttribute("userId");
        ProfileDTO profileDTO = service.getUserProfile(userId);
        return ResponseEntity.ok(profileDTO);
    }


    @GetMapping("/auth/get")
   public  ResponseEntity<?> get() {
        List<UserField> users = service.getUsers();
        return  ResponseEntity.ok(users);
    }
    @DeleteMapping("/auth/del/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        System.out.println(id);
        boolean deleted = service.deleteUser(id);
        return ResponseEntity.ok( deleted ? "user deleted" : "user with  not found"
        );
    }

}
