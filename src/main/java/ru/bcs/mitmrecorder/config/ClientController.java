package ru.bcs.mitmrecorder.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bcs.mitmrecorder.MessageCache;
import ru.bcs.mitmrecorder.MitmInterceptedMessage;

@Api(tags = { "client" })
@RestController
public class ClientController {

    @Autowired
    private MessageCache<String, MitmInterceptedMessage> messageCache;

    @GetMapping("/last")
    public @ResponseBody MitmInterceptedMessage getLastMessage(@RequestParam(value = "key", defaultValue = "") String key) {
        if (!key.isEmpty()) {
            return messageCache.getLastByKeyPart(key);
        }
        return messageCache.getLast();
    }

}
