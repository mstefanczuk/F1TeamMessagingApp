package pl.mstefanczuk.f1teammessagingapp.service;

import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;

public interface RaceProgressRecorder {

    void receiveCarInfo(final CarInfo carInfo);
}
