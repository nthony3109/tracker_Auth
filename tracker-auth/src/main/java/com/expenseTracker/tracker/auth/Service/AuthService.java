package com.expenseTracker.tracker.auth.Service;


import com.expenseTracker.tracker.auth.DTO.LoginDTO;
import com.expenseTracker.tracker.auth.DTO.RegisterDTO;
import com.expenseTracker.tracker.auth.Model.UserField;
import com.expenseTracker.tracker.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private  UserRepo userRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    JwtService jwtService;
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

    public String loginUser(LoginDTO loginReq) {
        //System.out.println("from service" + loginReq.getPassword() + " " + loginReq.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getUsername().trim(),loginReq.getPassword().trim()));
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(new HashMap<>(), user.getUsername());
        return  token;
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
}
