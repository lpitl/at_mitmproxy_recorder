package ru.bcs.mitmrecorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.bcs.mitmrecorder.config.MitmproxyConfiguration;

@SpringBootApplication
public class MitmRecorderApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(MitmRecorderApplication.class, args);
        context.getBean(MitmproxyConfiguration.class).startProxy();
    }

}
