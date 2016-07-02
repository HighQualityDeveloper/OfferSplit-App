package com.objectK;

/**
 * Created by Kinchit.
 */
public class objChatDataK {

    String from="";
    String to="";
    String text="";

    public String getJSON(String from, String to, String text) {
        return "{\"from\":\"" +from+ "\",\"to\":\"" +to+ "\",\"text\":\"" +text+ "\"}";
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
