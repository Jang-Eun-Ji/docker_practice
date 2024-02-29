package org.spring.Ordering.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {
    public static ResponseEntity<Map<String, Object>> MakeMessage(HttpStatus httpStatus, String message){
        Map<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(httpStatus.value()));
        body.put("status_message", httpStatus.getReasonPhrase());
        body.put("error_message", message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
