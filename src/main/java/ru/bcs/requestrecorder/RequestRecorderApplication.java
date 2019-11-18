package ru.bcs.requestrecorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.bcs.requestrecorder.config.MitmproxyConfiguration;

@SpringBootApplication
public class RequestRecorderApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(RequestRecorderApplication.class, args);
        context.getBean(MitmproxyConfiguration.class).startProxy();
    }

}
