package com.makarova.dto;

public class MessageDto {

    public String text;
    public String sender;

    public MessageDto(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }
}