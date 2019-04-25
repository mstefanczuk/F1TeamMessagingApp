package pl.mstefanczuk.f1teammessagingapp.service;

import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;

public interface MonitoringService {

    void receiveCarInfo(final CarInfo carInfo);
}
