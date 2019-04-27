package pl.mstefanczuk.f1teammessagingapp.service;

import javax.jms.TextMessage;

public interface TeamManagerService {

    void receiveRequest(TextMessage message);
}
