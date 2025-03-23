package org.example.fujitsudeliveryfeeapp.service;

import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.example.fujitsudeliveryfeeapp.model.City;
import org.example.fujitsudeliveryfeeapp.model.VehicleType;
import org.example.fujitsudeliveryfeeapp.repository.WeatherReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DeliveryFeeServiceTest {

    private final WeatherReportRepository repository = Mockito.mock(WeatherReportRepository.class);
    private final DeliveryFeeService service = new DeliveryFeeService(repository);

    @Test
    void calculateFee_withMissingWeatherReport_returnsMinusOne() {
        when(repository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku")).thenReturn(Optional.empty());

        DeliveryFeeResponse response = service.calculateFee(City.TALLINN, VehicleType.BIKE);

        assertThat(response.getTotalFee()).isEqualTo(-1.0);
    }

    @Test
    void calculateFee_withValidReport_returnsTotalFee() {
        WeatherReport report = new WeatherReport(
                null,
                "Tallinn-Harku",
                "12345",
                -15.0,
                5.0,
                "Clear",
                LocalDateTime.now()
        );

        when(repository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku")).thenReturn(Optional.of(report));

        DeliveryFeeResponse response = service.calculateFee(City.TALLINN, VehicleType.BIKE);

        // Base 3.0 + air temp extra 1.0 + wind 0.0 + phenomenon 0.0
        assertThat(response.getTotalFee()).isEqualTo(4.0);
    }

    @Test
    void calculateFee_forbiddenWindSpeed_returnsMinusOne() {
        WeatherReport report = new WeatherReport(
                null,
                "Tallinn-Harku",
                "12345",
                0.0,
                21.0,
                "Clear",
                LocalDateTime.now()
        );

        when(repository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku")).thenReturn(Optional.of(report));

        DeliveryFeeResponse response = service.calculateFee(City.TALLINN, VehicleType.BIKE);

        assertThat(response.getTotalFee()).isEqualTo(-1.0);
    }

    @Test
    void calculateFee_forbiddenPhenomenon_returnsMinusOne() {
        WeatherReport report = new WeatherReport(
                null,
                "Tallinn-Harku",
                "12345",
                0.0,
                5.0,
                "Thunderstorm",
                LocalDateTime.now()
        );

        when(repository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku")).thenReturn(Optional.of(report));

        DeliveryFeeResponse response = service.calculateFee(City.TALLINN, VehicleType.SCOOTER);

        assertThat(response.getTotalFee()).isEqualTo(-1.0);
    }
}
