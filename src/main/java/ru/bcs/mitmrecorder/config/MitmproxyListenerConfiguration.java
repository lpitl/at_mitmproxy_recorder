package ru.bcs.mitmrecorder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bcs.mitmrecorder.MessageCache;
import ru.bcs.mitmrecorder.MitmInterceptedMessage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Configuration
public class MitmproxyListenerConfiguration {

    @Autowired
    private MessageCache messageCache;

    @Value("${mitmproxy.port}")
    private int mitmproxyPort;

    @Value("${mitmproxy.path}")
    private String mitmproxyPath;

    @Value("${mitmproxy.store.active}")
    private Boolean storeActive;

    @Value("${mitmproxy.store.path}")
    private String storePath;

    @Bean
    MitmproxyJava mitmproxyListener() throws IOException, TimeoutException {
        MitmproxyJava proxy = new MitmproxyJava(mitmproxyPath, (InterceptedMessage message) -> {
            System.out.println("Intercepted request for " + message.getRequest().getUrl());

            MitmInterceptedMessage customMessage = new MitmInterceptedMessage(message);
            addObjectToMessageCache(customMessage);
            if (storeActive) {
                writeObjectToFile(customMessage);
            }
            return message;
        }, mitmproxyPort, null);

        proxy.start();

        return proxy;
    }

    private void addObjectToMessageCache(Object serObj) {
        try {
            messageCache.put(((MitmInterceptedMessage) serObj).getRequest().getUrl(), serObj);
            System.out.println("The Object was successfully written to message cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeObjectToFile(Object serObj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String output = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serObj);

            String filename = storePath + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss:SSS_").format(LocalDateTime.now()) + UUID.randomUUID().toString() + ".json";
            File messageFile = new File(filename);
            messageFile.getParentFile().mkdirs();
            messageFile.createNewFile();
            try (FileOutputStream fileOut = new FileOutputStream(filename, true)) {
                try (DataOutputStream dataOut = new DataOutputStream(fileOut)) {
                    dataOut.write(output.getBytes());
                    dataOut.flush();
                }
                fileOut.flush();
            }
            System.out.println("The Object was successfully written to file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
