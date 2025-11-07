package com.expenseTracker.tracker.auth.DTO;

import lombok.Data;

@Data
public class LoginRes {
    private  String accessToken;
    private String RefreshToken;
    private  Long  userId;


    public LoginRes(String token, String refreshToken, Long userId) {
        this.accessToken = token;
        this.RefreshToken =refreshToken;
        this.userId = userId;
    }


}
