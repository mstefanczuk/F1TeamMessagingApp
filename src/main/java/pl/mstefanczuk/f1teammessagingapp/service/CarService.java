package pl.mstefanczuk.f1teammessagingapp.service;

import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;

public interface CarService {

    void startRace();

    void publishCarInfo();

    void receiveReply(final String reply);

    void updateCarInfo(CarInfo carInfo);

    void requestForPitstop();
}
