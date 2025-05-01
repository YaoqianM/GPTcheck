package com.example.common.response;

import com.example.common.domain.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RestControllerAdvice
public class GenericResponse<T> {
    private int code;
    private String message;
    private LocalDateTime timestamp;
    private T data;

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .code(200)
                .message("Success")
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> error(int code, String message) {
        return GenericResponse.<T>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
public class GeneralResponseAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        GeneralResponse response = new GeneralResponse();
        response.setCode(0);
        response.setData(o);
        response.setTimestamp(new Date());
        return response;
    }
}
