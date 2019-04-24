package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.CarService;

import javax.annotation.PostConstruct;

@Service
public class CarServiceImpl implements CarService {

    private CarInfo carInfo;
    private long startTime;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public CarServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        jmsTemplate.setPubSubDomain(true);
    }

    @Override
    @PostConstruct
    public void startRace() {
        carInfo = new CarInfo();
        carInfo.setEngineTemperature(90);
        carInfo.setOilPressure(2);
        carInfo.setTyrePressure(2.5);
        startTime = System.currentTimeMillis();
    }

    @Override
    @Scheduled(fixedRate = 15000)
    public void publishCarInfo() {
        carInfo.setCurrentTime(System.currentTimeMillis() - startTime);
        jmsTemplate.convertAndSend(JmsConfig.PUBLISH_SUBSCRIBE_CHANNEL, carInfo);
    }

    @Override
    public void setHighEngineTemperature() {
        carInfo.setEngineTemperature(100);
    }

    @Override
    public void setDangerouslyHighEngineTemperature() {
        carInfo.setEngineTemperature(120);
    }

    @Override
    public void setCurrentTime() {
        carInfo.setCurrentTime(System.currentTimeMillis() - startTime);
    }
}
