package pl.mstefanczuk.f1teammessagingapp.service;

public interface CarService {

    public void startRace();
    public void publishCarInfo();
    public void setHighEngineTemperature();
    public void setDangerouslyHighEngineTemperature();
    public void setCurrentTime();
}
