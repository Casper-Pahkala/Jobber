package com.badfish.jobber.Models;

public class MessageModel {

    String senderID,name;
    String message;
    long timestamp;
    String currenttime, imageRef, lastText,lastTextTime;

    public MessageModel(String message, String senderID, Long timestamp, String imageRef) {
        this.senderID = senderID;
        this.message = message;
        this.timestamp = timestamp;
        this.imageRef = imageRef;

    }

    public String getImageRef() {
        return imageRef;
    }

    public String getName() {
        return name;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public MessageModel() {
    }
}
