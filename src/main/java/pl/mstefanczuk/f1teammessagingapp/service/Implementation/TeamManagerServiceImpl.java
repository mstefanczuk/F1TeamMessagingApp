package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.service.TeamManagerService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Random;

@Service
public class TeamManagerServiceImpl implements TeamManagerService {

    private static final String acceptMessage = "Udzielono zgody na zjazd do pit-stopu";
    private static final String rejectMessage = "Nie udzielono zgody na zjazd do pit-stopu";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public TeamManagerServiceImpl(@Qualifier("jmsQueueTemplate") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    @JmsListener(destination = JmsConfig.TEAM_MANAGER_CHANNEL, containerFactory = "jmsQueueListenerContainerFactory")
    public void receiveRequest(TextMessage requestMessage) {
        System.out.println("\n-- KIEROWNIK ZESPOŁU --");
        System.out.println("Została zgłoszona potrzeba zjazdu do pit-stopu");
        Random random = new Random();
        String reply = random.nextBoolean() ? acceptMessage : rejectMessage;

        try {
            System.out.println("Podejmowanie decyzji kierownika...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n-- KIEROWNIK ZESPOŁU --");
        System.out.println(reply);

        try {
            ActiveMQTextMessage replyMessage = new ActiveMQTextMessage();
            replyMessage.setJMSDestination(requestMessage.getJMSReplyTo());
            replyMessage.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
            replyMessage.setJMSReplyTo(requestMessage.getJMSReplyTo());
            replyMessage.setText(reply);

            jmsTemplate.convertAndSend(replyMessage.getJMSReplyTo(), replyMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
