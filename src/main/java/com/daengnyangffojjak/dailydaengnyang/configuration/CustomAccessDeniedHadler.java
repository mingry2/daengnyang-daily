package com.daengnyangffojjak.dailydaengnyang.configuration;

import com.daengnyangffojjak.dailydaengnyang.domain.dto.Response;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorCode;
import com.daengnyangffojjak.dailydaengnyang.exception.ErrorResponse;
import com.daengnyangffojjak.dailydaengnyang.exception.SecurityCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHadler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("엑세스 권한이 없습니다.");

        SecurityCustomException securityCustomException = new SecurityCustomException(ErrorCode.INVALID_PERMISSION);
        ErrorResponse errorResponse = new ErrorResponse(securityCustomException.getErrorCode(), securityCustomException.toString());
        Response<ErrorResponse> error = Response.error(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(error)); //Response객체를 response의 바디값으로 파싱


    }
}
