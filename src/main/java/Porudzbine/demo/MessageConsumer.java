package Porudzbine.demo;

import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    Queue queue;

    @JmsListener(destination = "ds.queue")
    public void receiveMessage(Message message) {
        System.out.println("Message received: " + message.getText());
    }
}
