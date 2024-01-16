package Porudzbine.demo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Message implements Serializable {
    private final String text;

    public Message(String text) {
        this.text = text;
    }

}
