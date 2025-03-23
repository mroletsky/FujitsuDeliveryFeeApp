package org.example.fujitsudeliveryfeeapp.service;

import lombok.RequiredArgsConstructor;
import org.example.fujitsudeliveryfeeapp.dto.DeliveryFeeResponse;
import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.example.fujitsudeliveryfeeapp.model.City;
import org.example.fujitsudeliveryfeeapp.model.VehicleType;
import org.example.fujitsudeliveryfeeapp.repository.WeatherReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryFeeService.class);

    private final WeatherReportRepository weatherReportRepository;

    /**
     * Method for calculating delivery fee based on latest weather data
     * @param city - location of delivery
     * @param vehicleType - type of vehicle for delivery
     * @return the delivery fee or -1.0 in case of forbidden vehicle type for selected city due to weather conditions
     */
    public DeliveryFeeResponse calculateFee(City city, VehicleType vehicleType) {
        double rbf = getRegionalBaseFee(city, vehicleType);

        String stationName = city.getStationName();

        Optional<WeatherReport> latestReportOptional = weatherReportRepository.findTopByStationNameOrderByTimestampDesc(stationName);

        if (latestReportOptional.isEmpty()) {
            return new DeliveryFeeResponse(-1.0); // fallback if no weather data is found
        }

        WeatherReport latestReport = latestReportOptional.get();

        double atef = calculateAirTemperatureExtraFee(vehicleType, latestReport.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, latestReport.getWindSpeed());
        double wpef = calculateWeatherPhenomenonExtraFee(vehicleType, latestReport.getPhenomenon());

        if (wsef == -1.0 || wpef == -1.0) {
            log.warn("Invalid weather conditions for vehicle type {}: windSpeed={}, phenomenon={}", vehicleType, latestReport.getWindSpeed(), latestReport.getPhenomenon());
            return new DeliveryFeeResponse(-1.0);
        }

        double total = rbf + atef + wsef + wpef;
        log.info("Calculated total delivery fee: {}", total);
        return new DeliveryFeeResponse(total);
    }

    private double getRegionalBaseFee(City city, VehicleType vehicleType) {
        double baseFee = switch (city) {
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
        log.debug("Regional base fee for {} and {} is {}", city, vehicleType, baseFee);
        return baseFee;
    }

    private double calculateAirTemperatureExtraFee(VehicleType vehicleType, double airTemperature) {
        log.debug("Calculating air temperature extra fee for vehicle: {} with temperature: {}", vehicleType, airTemperature);
        if (vehicleType == VehicleType.CAR) return 0.0;

        if (airTemperature < -10) return 1.0;
        if (airTemperature < 0) return 0.5;
        return 0.0;
    }

    private double calculateWindSpeedExtraFee(VehicleType vehicleType, double windSpeed) {
        log.debug("Calculating wind speed extra fee for vehicle: {} with wind speed: {}", vehicleType, windSpeed);
        if (vehicleType != VehicleType.BIKE) return 0.0;

        if (windSpeed > 20) return -1.0;
        if (windSpeed > 10) return 0.5;
        return 0.0;
    }

    private double calculateWeatherPhenomenonExtraFee(VehicleType vehicleType, String phenomenon) {
        log.debug("Calculating weather phenomenon extra fee for vehicle: {} with phenomenon: {}", vehicleType, phenomenon);
        if (vehicleType == VehicleType.CAR) return 0.0;

        phenomenon = phenomenon.toLowerCase();

        if (phenomenon.contains("snow") || phenomenon.contains("sleet")) return 1.0;
        if (phenomenon.contains("rain")) return 0.5;
        if (phenomenon.contains("glaze") || phenomenon.contains("hail") || phenomenon.contains("thunder")) return -1.0;
        return 0.0;
    }

}


