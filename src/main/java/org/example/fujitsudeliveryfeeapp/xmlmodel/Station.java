package org.example.fujitsudeliveryfeeapp.xmlmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Station {
    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "wmocode")
    private String wmoCode;

    @XmlElement(name = "airtemperature")
    private double airTemperature;

    @XmlElement(name = "windspeed")
    private double windSpeed;

    @XmlElement(name = "phenomenon")
    private String phenomenon;
}
