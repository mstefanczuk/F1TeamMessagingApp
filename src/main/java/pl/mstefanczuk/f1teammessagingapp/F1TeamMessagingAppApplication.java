package pl.mstefanczuk.f1teammessagingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class F1TeamMessagingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(F1TeamMessagingAppApplication.class, args);
    }

}
