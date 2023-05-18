package Shared;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import Shared.Eventtype;

public class Eventmessage implements Serializable 
{
    private Eventtype topic_content;
    private String message_text;
    private LocalDateTime timestamp;

    public Eventmessage(Eventtype topic_content,String message_text) {
        this.topic_content = topic_content;
        this.message_text = message_text;
        this.timestamp = LocalDateTime.now();
    }
    
    
    public Eventtype get_topiccontent() {
        return topic_content;
    }


    @Override
    public String toString() {
        
        return String.format("%s"+ " Publisher Message is :- " + " %s \n ", this.topic_content, this.message_text);
    }
}

