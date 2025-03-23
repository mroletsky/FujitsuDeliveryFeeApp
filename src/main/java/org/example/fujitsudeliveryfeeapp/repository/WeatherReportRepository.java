package org.example.fujitsudeliveryfeeapp.repository;

import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {
    Optional<WeatherReport> findTopByStationNameOrderByTimestampDesc(String stationName);
}
