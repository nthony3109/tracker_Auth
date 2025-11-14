package com.expenseTracker.tracker.auth.Controller;

import com.expenseTracker.tracker.auth.DTO.*;
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

    // to register user
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

    // login user and generate token
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
  // to get all users
    @GetMapping("/auth/get")
   public  ResponseEntity<?> get() {
        List<UserField> users = service.getUsers();
        return  ResponseEntity.ok(users);
    }
    //delete user by id
    @DeleteMapping("/auth/del/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        System.out.println(id);
        boolean deleted = service.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("user deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with  not found");
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshTokenReq req) {
        System.out.println(req.getTokenRefresher());
        RefreshedToken refreshedToken = service.reGenerateAccessToken(req.getTokenRefresher());
        return ResponseEntity.status(HttpStatus.CREATED).body(refreshedToken);
    }

    // logout user by refresh token deletion
    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshTokenReq req) {
        boolean isLoggedOut = service.logOutUser(req.getTokenRefresher());
        if (!isLoggedOut) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("logout failed");
        }
        return ResponseEntity.ok("user logged out successfully");
    }

}
