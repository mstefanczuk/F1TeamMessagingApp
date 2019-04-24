package pl.mstefanczuk.f1teammessagingapp.service.Implementation;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.f1teammessagingapp.config.JmsConfig;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.RaceProgressRecorder;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RaceProgressRecorderImpl implements RaceProgressRecorder {

    private String logFilename;

    @Override
    @JmsListener(destination = JmsConfig.PUBLISH_SUBSCRIBE_CHANNEL, containerFactory = "jmsTopicListenerContainerFactory")
    public void receiveCarInfo(final CarInfo carInfo) {
        try {
            FileWriter fw = new FileWriter(logFilename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter printWriter = new PrintWriter(bw);

            printWriter.println("Engine temperature: " + carInfo.getEngineTemperature());
            printWriter.println("Oil pressure: " + carInfo.getOilPressure());
            printWriter.println("Tyre pressure: " + carInfo.getTyrePressure());
            printWriter.println("Current time: " + getFormattedCurrentTime(carInfo.getCurrentTime()));
            printWriter.println();

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        logFilename = "race_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".log";
    }

    private String getFormattedCurrentTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        return hours + ":" + minutes + ":" + seconds;
    }
}
