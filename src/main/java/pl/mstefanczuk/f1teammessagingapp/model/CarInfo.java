package pl.mstefanczuk.f1teammessagingapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarInfo implements Serializable {

    private double engineTemperature;
    private double tyrePressure;
    private double oilPressure;
    private long currentTime;
}
