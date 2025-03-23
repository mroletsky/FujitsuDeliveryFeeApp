package org.example.fujitsudeliveryfeeapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeRequest;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.service.DeliveryFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryFeeController.class);

    private final DeliveryFeeService deliveryFeeService;

    @PostMapping
    public ResponseEntity<?> calculateFee(@RequestBody DeliveryFeeRequest request) {
        DeliveryFeeResponse response = deliveryFeeService.calculateFee(request.getCity(), request.getVehicleType());

        if (response.getTotalFee() == -1.0) {
            log.warn("Forbidden vehicle type or missing weather data for city: {}, vehicle: {}", request.getCity(), request.getVehicleType());
            return ResponseEntity.badRequest().body("Usage of selected vehicle type is forbidden");
        } else {
            log.info("Successfully calculated delivery fee: {}", response.getTotalFee());
            return ResponseEntity.ok(response);
        }
    }
}
