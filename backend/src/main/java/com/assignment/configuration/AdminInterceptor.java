package com.assignment.configuration;

import com.assignment.response.Response;
import com.assignment.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.assignment.response.ResponseStatus.FORBIDDEN;
import static com.assignment.response.ResponseStatus.UNAUTHORIZED_ACCESS_TOKEN;

@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtService jwtService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private void setResponseAccessDenied(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=utf-8");
        String result = objectMapper.writeValueAsString(
                new Response<>(FORBIDDEN)
        );
        response.getWriter().write(result);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            return true;
        }
        final String accessToken = jwtService.getAccessToken();
        if(jwtService.getUser().getGrade().equals("admin"))
            return true;
        else {
            setResponseAccessDenied(response);
            return false;
        }

    }
}
