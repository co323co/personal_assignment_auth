package com.assignment.serviceImpl;

import com.assignment.dao.AuthRepository;
import com.assignment.entity.Auth;
import io.jsonwebtoken.*;
import com.assignment.configuration.ValidationCheck;
import com.assignment.entity.User;
import com.assignment.service.JwtService;
import com.assignment.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;

@Service("JwtService")
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Value("${custom.constant.token.secret.key}")
    private String TOKEN_SECRET_KEY;

//    private long ACCESS_TOKEN_VALIDITY_SECONDS = 60*60;
    private long ACCESS_TOKEN_VALIDITY_SECONDS = 10;
//    private long REFRESH_TOKEN_VALIDITY_SECONDS = 60*60*24*14;
    private long REFRESH_TOKEN_VALIDITY_SECONDS = 20;

    public String createAccessToken(int userId) {
        Date now = new Date();
        return Jwts.builder().claim("userId", userId).setIssuedAt(now)
                 .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET_KEY).compact();
    }

    public String createRefreshToken(int userId) {
        Date now = new Date();
        String refreshToken = Jwts.builder().claim("userId", userId).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET_KEY).compact();
        User user = userRepository.findById(userId).orElse(null);
        if(user==null) return null;
        Auth auth = authRepository.findByUserId(userId)
                .orElse(Auth.builder().user(user).build());
        auth.setRefreshToken(refreshToken);
        authRepository.save(auth);
        return refreshToken;
    }

    public String refreshAccessToken() {
        String refreshToken = getRefreshToken();
        //refresh token이 유효하면 access token 재발급
        if(validateToken(refreshToken) && !validateTokenExceptExpiration(refreshToken)){
            Jws<Claims> claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(refreshToken);
            int userId = claims.getBody().get("userId", Integer.class);

            //db에 저장되어있는 refresh token이랑 일치하는지 검증
            Auth dbAuth = authRepository.findByUserId(userId).orElse(null);
            if(dbAuth==null || !refreshToken.equals(dbAuth.getRefreshToken()))
                return null;
            return createAccessToken(userId);
        }
         else return null;
    }


    @Override
    public String getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    @Override
    public String getRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return request.getHeader("X-REFRESH-TOKEN");
    }

    @Override
    public int getUserId() {
        String accessToken = getAccessToken();
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(accessToken);
            if (accessToken == null)
                return -1;

            int userId = claims.getBody().get("userId", Integer.class);
            if (!ValidationCheck.isValidId(userId))
                return -3;

            User userDB = userRepository.findById(userId).orElse(null);
            if (userDB == null)
                return -3;

            return userId;
        } catch (Exception exception) {
            return -1;
        }
    }

    @Override
    public int getUserId(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(jwtToken);
            if (jwtToken == null)
                return -1;

            int userId = claims.getBody().get("userId", Integer.class);
            if (!ValidationCheck.isValidId(userId))
                return -3;

            User userDB = userRepository.findById(userId).orElse(null);
            if (userDB == null)
                return -3;

            return userId;
        } catch (Exception exception) {
            return -1;
        }
    }

    @Override
    public User getUser() {
        String accessToken = getAccessToken();
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(accessToken);
            if (accessToken == null)
                return null;

            int userId = claims.getBody().get("userId", Integer.class);
            if (!ValidationCheck.isValidId(userId))
                return null;
            User userDB = userRepository.findById(userId).orElse(null);
            if (userDB == null)
                return null;
            return userDB;
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    // Jwt Token의 유효성 검사 (만료되었어도 OK)
    public boolean validateToken(String jwtToken) {
        if(jwtToken==null) return false;
        try{
            System.out.println("Claims : " + this.getClaims(jwtToken));
            if(this.getClaims(jwtToken) == null) return false;
        } catch (SignatureException ex) {
            return false;
        } catch (MalformedJwtException ex) {
            return false;
        } catch (UnsupportedJwtException ex) {
            return false;
        } catch (IllegalArgumentException ex) {
            return false;
        } catch (ExpiredJwtException ex) {
            return true;
        }
        return true;
    }

    @Override
    // Jwt Token이 만료되었는지 검사
    public boolean validateTokenExceptExpiration(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(jwtToken);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    @Override
    public Jws<Claims> getClaims(String jwtToken) {
        try {
            return Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(jwtToken);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw ex;
        }
    }

}