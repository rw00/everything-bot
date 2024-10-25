package com.rw.bots.everything;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EverythingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverythingBotApplication.class, args);
    }
}
