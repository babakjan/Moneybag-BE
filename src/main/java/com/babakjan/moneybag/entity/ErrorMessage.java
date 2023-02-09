package com.babakjan.moneybag.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private HttpStatus status;

    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status) {
        this.status = status;
        this.errors = new HashMap<>();
    }
}
