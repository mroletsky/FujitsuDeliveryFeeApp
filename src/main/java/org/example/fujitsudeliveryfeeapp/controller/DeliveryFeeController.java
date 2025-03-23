package org.example.fujitsudeliveryfeeapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeRequest;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.service.DeliveryFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    @PostMapping
    public ResponseEntity<?> calculateFee(@RequestBody DeliveryFeeRequest request) {
        DeliveryFeeResponse response = deliveryFeeService.calculateFee(request.getCity(), request.getVehicleType());

        if (response.getTotalFee() == -1.0) {
            return ResponseEntity.badRequest().body("Usage of selected vehicle type is forbidden");
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
