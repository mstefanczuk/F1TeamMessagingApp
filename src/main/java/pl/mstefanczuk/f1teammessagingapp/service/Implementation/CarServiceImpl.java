package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.Request;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.CarService;

import javax.annotation.PostConstruct;

@Service
public class CarServiceImpl implements CarService {

    private CarInfo carInfo;
    private long startTime;

    private final JmsTemplate jmsQueueTemplate;

    private final JmsTemplate jmsTopicTemplate;

    @Autowired
    public CarServiceImpl(@Qualifier("jmsQueueTemplate") JmsTemplate jmsQueueTemplate,
                          @Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
        this.jmsTopicTemplate = jmsTopicTemplate;
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
        setCurrentTime();
        jmsTopicTemplate.convertAndSend(JmsConfig.PUBLISH_SUBSCRIBE_CHANNEL, carInfo);
    }

    @Override
    @JmsListener(destination = JmsConfig.CAR_REPLY_CHANNEL, containerFactory = "jmsQueueListenerContainerFactory")
    public void receiveReply(final String reply) {
        System.out.println("\n-- BOLID --");
        System.out.println(reply);
    }

    @Override
    public void updateCarInfo(CarInfo carInfo) {
        this.carInfo.setEngineTemperature(carInfo.getEngineTemperature());
        this.carInfo.setOilPressure(carInfo.getOilPressure());
        this.carInfo.setTyrePressure(carInfo.getTyrePressure());
    }

    @Override
    public void requestForPitstop() {
        System.out.println("\n-- BOLID --");
        System.out.println("Zgłaszanie potrzeby zjazdu do pit-stopu");
        jmsQueueTemplate.convertAndSend(JmsConfig.TEAM_MANAGER_CHANNEL, Request.PITSTOP);
    }

    private void setCurrentTime() {
        carInfo.setCurrentTime(System.currentTimeMillis() - startTime);
    }
}
