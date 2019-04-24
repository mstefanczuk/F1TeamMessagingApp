package pl.mstefanczuk.f1teammessagingapp.service;

import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;

public interface RaceProgressRecorder {
    public void receiveCarInfo(final CarInfo carInfo);
}
