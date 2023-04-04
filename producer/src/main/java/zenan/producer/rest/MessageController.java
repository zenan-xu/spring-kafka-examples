package zenan.producer.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zenan.producer.model.Payload;
import zenan.producer.service.MessageService;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "send", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestBody Payload payload) {
        messageService.sendMessage(payload);
    }

    @PostMapping(value = "sendWithException", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessageWithException(@RequestBody Payload payload) {
        messageService.sendMessageWithException(payload);
    }
}
