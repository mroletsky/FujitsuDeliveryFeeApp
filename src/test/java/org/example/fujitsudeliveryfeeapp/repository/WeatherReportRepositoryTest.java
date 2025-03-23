package org.example.fujitsudeliveryfeeapp.repository;

import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WeatherReportRepositoryTest {

    @Autowired
    private WeatherReportRepository repository;

    @Test
    void findTopByStationNameOrderByTimestampDesc_shouldReturnLatestReport() {
        String stationName = "Tallinn-Harku";

        WeatherReport older = new WeatherReport(
                null,
                stationName,
                "12345",
                -5.0,
                8.0,
                "Snow",
                LocalDateTime.now().minusHours(3)
        );

        WeatherReport latest = new WeatherReport(
                null, stationName,
                "12345",
                -2.0,
                12.0,
                "Snow",
                LocalDateTime.now()
        );

        repository.save(older);
        repository.save(latest);

        var result = repository.findTopByStationNameOrderByTimestampDesc(stationName);

        assertThat(result).isPresent();
        assertThat(result.get().getTimestamp()).isEqualTo(latest.getTimestamp());
    }
}
