package com.expenseTracker.tracker.auth.Config;


import com.expenseTracker.tracker.auth.Service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2Handler implements AuthenticationSuccessHandler {

    private  final JwtService jwtService;

//    @Value("${app.frontend.redirect-uri:http://localhost:3000/oauth2/redirect}")
//    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttribute("email");

        if (email == null) {
            Object sub = oAuth2User.getAttribute("sub");
                    email = sub != null ? sub.toString() : null;
        }
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"email not found ");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", "user");
        claims.put("provider", "google");

        String token = jwtService.generateToken(claims,email);


        // for frontend redirection
//        String target = frontendRedirectUri + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
//        response.sendRedirect(target);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{ \"token\": \"" + token + "\", \"email\": \"" + email + "\" }"
        );
    }
}

