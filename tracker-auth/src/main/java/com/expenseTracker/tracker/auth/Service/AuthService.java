package com.expenseTracker.tracker.auth.Service;


import com.expenseTracker.tracker.auth.DTO.*;
import com.expenseTracker.tracker.auth.Model.Rtoken;
import com.expenseTracker.tracker.auth.Model.UserField;
import com.expenseTracker.tracker.auth.repo.RtokenRepo;
import com.expenseTracker.tracker.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private  UserRepo userRepo;
    @Autowired
    RtokenRepo rtokenRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    RtokenService rtokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
   UserField user = new UserField();

    public boolean registerUser(RegisterDTO regDTO) {
        user.setFirstname(regDTO.getFirstname());
        user.setLastname(regDTO.getLastname());
        user.setEmail(regDTO.getEmail());
        user.setUsername(regDTO.getUsername());
        user.setPassword(encoder.encode(regDTO.getPassword()));
        user.setPhone(regDTO.getPhone());
      //   System.out.println("from the service" + user);
        UserField registeredUser = userRepo.save(user);
      //  System.out.println("from Service" + registeredUser.getUsername());
    return  registeredUser.getUsername() != null;
    }

    public LoginRes loginUser(LoginDTO loginReq) {
        //System.out.println("from service" + loginReq.getPassword() + " " + loginReq.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getUsername().trim(),loginReq.getPassword().trim()));
        UserDetails user = (UserDetails) authentication.getPrincipal();
        UserField user1 = userRepo.findByUsername(user.getUsername()) .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user1.getId());
        claims.put("Provider", "local");
        String accessToken = jwtService.generateToken(claims, user.getUsername());
        Rtoken rtoken = rtokenService.generateRtoken(user1);
        String refreshToken = rtoken.getRefreshToken();
        return  new LoginRes(accessToken, refreshToken, user1.getId());
    }

    public List<UserField> getUsers() {
        //System.out.println(userRepo.findAll());
      return   userRepo.findAll();

    }

    public boolean deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public ProfileDTO getUserProfile(Long userId) {
        ProfileDTO profileDTO= new ProfileDTO();
        Optional<UserField> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("user not found");
        }
        profileDTO.setEmail(user.get().getEmail());
        profileDTO.setFirstname(user.get().getFirstname());
        profileDTO.setLastname(user.get().getLastname());
        profileDTO.setUsername(user.get().getUsername());
        System.out.println(profileDTO);
        System.out.println("data from user: " + user.get().getUsername());
        return profileDTO;
    }
    // to refresh accessToken
    public RefreshedToken reGenerateAccessToken(String token) {
        boolean isValid = rtokenService.isTokenValid(token);
        if (!isValid) {
            throw new RuntimeException("invalid refresh token");
        }
        UserField userField = rtokenRepo.findByRefreshToken(token).get().getUser();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("Provider", "local");
        String newAccessToken = jwtService.generateToken(claims, user.getUsername());
        return new RefreshedToken(newAccessToken, token);
    }
}
