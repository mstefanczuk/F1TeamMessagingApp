package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.CarService;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.util.Random;

@Service
public class CarServiceImpl implements CarService {

    private CarInfo carInfo;
    private long startTime;

    private final JmsTemplate jmsTopicTemplate;

    private final ActiveMQConnectionFactory activeMQConnectionFactory;

    @Autowired
    public CarServiceImpl(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate,
                          ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.jmsTopicTemplate = jmsTopicTemplate;
        this.activeMQConnectionFactory = activeMQConnectionFactory;
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
    @Scheduled(fixedRate = 5000)
    public void publishCarInfo() {
        setCurrentTime();
        jmsTopicTemplate.convertAndSend(JmsConfig.CAR_INFO_CHANNEL, carInfo);
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

        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = new ActiveMQQueue(JmsConfig.TEAM_MANAGER_CHANNEL);
            MessageProducer producer = session.createProducer(destination);

            Destination replyDestination = session.createTemporaryQueue();

            TextMessage message = session.createTextMessage("pit-stop-request");
            message.setJMSReplyTo(replyDestination);
            message.setJMSCorrelationID(Long.toHexString(new Random(System.currentTimeMillis()).nextLong()));
            producer.send(message);

            MessageConsumer consumer = session.createConsumer(replyDestination);
            ActiveMQTextMessage reply = (ActiveMQTextMessage) consumer.receive();
            System.out.println("ODPOWIEDŹ: " + reply.getText());

            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentTime() {
        carInfo.setCurrentTime(System.currentTimeMillis() - startTime);
    }
}
