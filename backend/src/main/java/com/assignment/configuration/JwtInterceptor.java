package com.assignment.configuration;

import com.assignment.exception.UnauthorizedException;
import com.assignment.response.Response;
import com.assignment.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.assignment.response.ResponseStatus.*;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtService jwtService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private void setResponseAccessDenied(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        String result = objectMapper.writeValueAsString(
                new Response<>(UNAUTHORIZED_ACCESS_TOKEN)
        );
        response.getWriter().write(result);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            return true;
        }
        final String accessToken = jwtService.getAccessToken();
        //access 토큰이 유효하고 만료되지 않음
        if(jwtService.validateToken(accessToken) && !jwtService.validateTokenExceptExpiration(accessToken)){
            return true;
        }
        else{ //access 토근이 유효하지 않음
            setResponseAccessDenied(response);
            System.out.println("토큰이 유효하지 않음 "+jwtService.getAccessToken());
            return false;
//            throw new UnauthorizedException();
        }
    }
}
