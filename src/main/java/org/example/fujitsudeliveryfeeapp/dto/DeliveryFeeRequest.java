package org.example.fujitsudeliveryfeeapp.dto;

import lombok.Data;
import org.example.fujitsudeliveryfeeapp.model.City;
import org.example.fujitsudeliveryfeeapp.model.VehicleType;

@Data
public class DeliveryFeeRequest {
    private City city;
    private VehicleType vehicleType;
}

