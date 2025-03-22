package org.example.fujitsudeliveryfeeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FujitsuDeliveryFeeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsuDeliveryFeeAppApplication.class, args);
    }

}
