package com.objectK;

/**
 * Created by Kinchit.
 */
public class objChatK {

    String from="";
    String text="";

    public objChatK(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
