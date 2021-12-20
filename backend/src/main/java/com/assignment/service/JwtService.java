package com.assignment.service;

import com.assignment.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
    <T> String createAccessToken(int userId);
    <T> String createRefreshToken(int userId);
    String getAccessToken();
    String getRefreshToken();
    String refreshAccessToken();
    int getUserId();
    int getUserId(String jwtToken);
    User getUser();
    boolean validateToken(String jwtToken);
    boolean validateTokenExceptExpiration(String jwtToken);
    Jws<Claims> getClaims(String jwtToken);
}