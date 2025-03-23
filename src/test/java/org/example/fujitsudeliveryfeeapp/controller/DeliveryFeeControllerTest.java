package org.example.fujitsudeliveryfeeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeRequest;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.model.City;
import org.example.fujitsudeliveryfeeapp.model.VehicleType;
import org.example.fujitsudeliveryfeeapp.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeliveryFeeService deliveryFeeService;

    @InjectMocks
    private DeliveryFeeController deliveryFeeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryFeeController).build();
    }

    @Test
    void calculateFee_shouldReturn200AndFee() throws Exception {
        when(deliveryFeeService.calculateFee(eq(City.TALLINN), eq(VehicleType.BIKE)))
                .thenReturn(new DeliveryFeeResponse(4.0));

        DeliveryFeeRequest request = new DeliveryFeeRequest();
        request.setCity(City.TALLINN);
        request.setVehicleType(VehicleType.BIKE);

        mockMvc.perform(post("/api/delivery-fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(4.0));
    }

    @Test
    void calculateFee_forbiddenCondition_shouldReturn400() throws Exception {
        when(deliveryFeeService.calculateFee(eq(City.TALLINN), eq(VehicleType.BIKE)))
                .thenReturn(new DeliveryFeeResponse(-1.0));

        DeliveryFeeRequest request = new DeliveryFeeRequest();
        request.setCity(City.TALLINN);
        request.setVehicleType(VehicleType.BIKE);

        mockMvc.perform(post("/api/delivery-fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usage of selected vehicle type is forbidden"));
    }
}