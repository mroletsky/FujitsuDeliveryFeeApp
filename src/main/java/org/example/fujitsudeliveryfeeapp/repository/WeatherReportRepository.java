package org.example.fujitsudeliveryfeeapp.repository;

import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {
}
