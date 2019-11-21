package ru.bcs.mitmrecorder.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bcs.mitmrecorder.MessageCache;
import ru.bcs.mitmrecorder.MitmInterceptedMessage;

@Api(tags = { "client" })
@RestController
public class ClientController {

    @Autowired
    private MessageCache<String, MitmInterceptedMessage> messageCache;

    @GetMapping("/last")
    public @ResponseBody ResponseEntity<MitmInterceptedMessage> getLastMessage(@RequestParam(value = "key", defaultValue = "") String key) {
        MitmInterceptedMessage mitmInterceptedMessage;

        if (!key.isEmpty()) {
            mitmInterceptedMessage = messageCache.getLastByKeyPart(key);
        } else {
            mitmInterceptedMessage = messageCache.getLast();
        }

        if (mitmInterceptedMessage != null) {
            return ResponseEntity.status(HttpStatus.OK).body(mitmInterceptedMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @DeleteMapping("/clean")
    public void cleanMessageCache() {
        messageCache.removeAll();
    }

}
