package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.Request;
import pl.mstefanczuk.f1teammessagingapp.service.TeamManagerService;

import java.util.Random;

@Service
public class TeamManagerServiceImpl implements TeamManagerService {

    private static final String acceptMessage = "Udzielono zgody na zjazd do pit-stopu";
    private static final String rejectMessage = "Nie udzielono zgody na zjazd do pit-stopu";

    @Override
    @JmsListener(destination = JmsConfig.TEAM_MANAGER_CHANNEL, containerFactory = "jmsQueueListenerContainerFactory")
    @SendTo(JmsConfig.CAR_REPLY_CHANNEL)
    public String receiveRequest(Request request) {
        System.out.println("\n-- KIEROWNIK ZESPOŁU --");

        String reply = null;
        if (request.equals(Request.PITSTOP)) {
            System.out.println("Została zgłoszona potrzeba zjazdu do pit-stopu");
            Random random = new Random();
            reply = random.nextBoolean() ? acceptMessage : rejectMessage;
            System.out.println(reply);
        }

        return reply;
    }
}
