package org.example.fujitsudeliveryfeeapp.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.example.fujitsudeliveryfeeapp.entity.WeatherReport;
import org.example.fujitsudeliveryfeeapp.repository.WeatherReportRepository;
import org.example.fujitsudeliveryfeeapp.xmlmodel.Observations;
import org.example.fujitsudeliveryfeeapp.xmlmodel.Station;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

@Component
public class WeatherReportFetcher {

    @Value("${weather.fetch.url}")
    private String url;

    private final WeatherReportRepository repository;

    public WeatherReportFetcher(WeatherReportRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    @Scheduled(cron = "${fetch.cron.expression}")
    @Transactional
    public void fetchAndSaveWeatherReports() {
        try {
            URI uri = new URI(url);
            URL urlObj = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Observations.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    Observations observations = (Observations) unmarshaller.unmarshal(inputStream);
                    for (Station station : observations.getStations()) {
                        if (isTargetStation(station.getName())) {
                            WeatherReport report = new WeatherReport();
                            report.setStationName(station.getName());
                            report.setWmoCode(station.getWmoCode());
                            report.setAirTemperature(station.getAirTemperature());
                            report.setWindSpeed(station.getWindSpeed());
                            report.setPhenomenon(station.getPhenomenon());
                            report.setTimestamp(LocalDateTime.now());
                            repository.save(report);
                        }
                    }
                    System.out.println("Weather reports fetched and saved successfully.");
                }
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and save weather reports", e);
        }
    }

    private boolean isTargetStation(String name) {
        return "Tallinn-Harku".equalsIgnoreCase(name)
                || "Tartu-Tõravere".equalsIgnoreCase(name)
                || "Pärnu".equalsIgnoreCase(name);
    }
}
