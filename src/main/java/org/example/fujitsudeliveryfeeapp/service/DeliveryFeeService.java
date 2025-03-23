package org.example.fujitsudeliveryfeeapp.service;

import lombok.RequiredArgsConstructor;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.example.fujitsudeliveryfeeapp.model.City;
import org.example.fujitsudeliveryfeeapp.model.VehicleType;
import org.example.fujitsudeliveryfeeapp.repository.WeatherReportRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {

    private final WeatherReportRepository weatherReportRepository;

    public DeliveryFeeResponse calculateFee(City city, VehicleType vehicleType) {
        double rbf = getRegionalBaseFee(city, vehicleType);

        String stationName = city.getStationName();

        Optional<WeatherReport> latestReportOptional = weatherReportRepository.findTopByStationNameOrderByTimestampDesc(stationName);

        // Handle missing weather report
        if (latestReportOptional.isEmpty()) {
            return new DeliveryFeeResponse(-1.0); // fallback if no weather data is found
        }

        WeatherReport latestReport = latestReportOptional.get();

        double atef = calculateAirTemperatureExtraFee(vehicleType, latestReport.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, latestReport.getWindSpeed());
        double wpef = calculateWeatherPhenomenonExtraFee(vehicleType, latestReport.getPhenomenon());

        if (wsef == -1.0 || wpef == -1.0) {
            return new DeliveryFeeResponse(-1.0);
        }

        double total = rbf + atef + wsef + wpef;
        return new DeliveryFeeResponse(total);
    }

    private double getRegionalBaseFee(City city, VehicleType vehicleType) {
        return switch (city) {
            case TALLINN -> switch (vehicleType) {
                case CAR -> 4.0;
                case SCOOTER -> 3.5;
                case BIKE -> 3.0;
            };
            case TARTU -> switch (vehicleType) {
                case CAR -> 3.5;
                case SCOOTER -> 3.0;
                case BIKE -> 2.5;
            };
            case PARNU -> switch (vehicleType) {
                case CAR -> 3.0;
                case SCOOTER -> 2.5;
                case BIKE -> 2.0;
            };
        };
    }

    private double calculateAirTemperatureExtraFee(VehicleType vehicleType, double airTemperature) {
        if (vehicleType == VehicleType.CAR) return 0.0;

        if (airTemperature < -10) return 1.0;
        if (airTemperature < 0) return 0.5;
        return 0.0;
    }

    private double calculateWindSpeedExtraFee(VehicleType vehicleType, double windSpeed) {
        if (vehicleType != VehicleType.BIKE) return 0.0;

        if (windSpeed > 20) return -1.0;
        if (windSpeed > 10) return 0.5;
        return 0.0;
    }

    private double calculateWeatherPhenomenonExtraFee(VehicleType vehicleType, String phenomenon) {
        if (vehicleType == VehicleType.CAR) return 0.0;

        phenomenon = phenomenon.toLowerCase();

        if (phenomenon.contains("snow") || phenomenon.contains("sleet")) return 1.0;
        if (phenomenon.contains("rain")) return 0.5;
        if (phenomenon.contains("glaze") || phenomenon.contains("hail") || phenomenon.contains("thunder")) return -1.0;
        return 0.0;
    }

}


