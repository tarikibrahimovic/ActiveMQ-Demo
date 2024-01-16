package Porudzbine.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final MessageProducer messageProducer;

    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        Message message1 = new Message(message);
        try {
            messageProducer.sendMessage(message1);
            return ResponseEntity.ok("Message sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Message not sent");
        }
    }
}
