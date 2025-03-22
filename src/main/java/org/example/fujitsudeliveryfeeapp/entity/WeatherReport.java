package org.example.fujitsudeliveryfeeapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;
    private String wmoCode;
    private Double airTemperature;
    private Double windSpeed;
    private String phenomenon;
    private LocalDateTime timestamp;
}
