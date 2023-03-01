package com.babakjan.moneybag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MoneybagApplication {

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String displayAppIsRunning() {
        return "Moneybag BE is running";
    }

    public static void main(String[] args) {
        SpringApplication.run(MoneybagApplication.class, args);
    }
}
