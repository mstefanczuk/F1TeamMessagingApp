package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.service.MechanicsTeamService;

@Service
public class MechanicsTeamServiceImpl implements MechanicsTeamService {

    @Override
    @JmsListener(destination = JmsConfig.MECHANICS_TEAM_CHANNEL, containerFactory = "jmsQueueListenerContainerFactory")
    public void receiveMessage(final String message) {
        System.out.println("\n-- ZESPÓŁ MECHANIKÓW --");
        System.out.println(message);
    }
}
