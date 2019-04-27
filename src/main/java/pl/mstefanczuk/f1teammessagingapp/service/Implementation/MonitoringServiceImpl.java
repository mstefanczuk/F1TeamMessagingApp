package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.MonitoringService;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MonitoringServiceImpl(@Qualifier("jmsQueueTemplate") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    @JmsListener(destination = JmsConfig.CAR_INFO_CHANNEL, containerFactory = "jmsTopicListenerContainerFactory")
    public void receiveCarInfo(final CarInfo carInfo) {
        String replyMessage = "";
        String messageForMechanicsTeam = "";
        if (carInfo.getEngineTemperature() > 90 && carInfo.getEngineTemperature() <= 100) {
            replyMessage += "Ostrzeżenie: podniesiona temperatura silnika\n";
        }
        if (carInfo.getEngineTemperature() > 100) {
            replyMessage += "ZAGROŻENIE: bardzo wysoka temperatura silnika\n";
            messageForMechanicsTeam += "AWARIA SILNIKA: bardzo wysoka temperatura silnika\n";
        }
        if (carInfo.getOilPressure() < 1.5 || carInfo.getOilPressure() > 3) {
            replyMessage += "Ostrzeżenie: ciśnienie oleju poza normą\n";
        }
        if (carInfo.getTyrePressure() < 1.5 || carInfo.getTyrePressure() > 3) {
            replyMessage += "Ostrzeżenie: ciśnienie w oponach poza normą\n";
        }

        if (!replyMessage.isEmpty()) {
            jmsTemplate.convertAndSend(JmsConfig.CAR_REPLY_CHANNEL, replyMessage);
        }

        if (!messageForMechanicsTeam.isEmpty()) {
            jmsTemplate.convertAndSend(JmsConfig.MECHANICS_TEAM_CHANNEL, messageForMechanicsTeam);
        }
    }
}
