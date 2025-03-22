package org.example.fujitsudeliveryfeeapp.xmlmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@XmlRootElement(name = "observations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observations {
    @XmlElement(name = "station")
    private List<Station> stations;
}
