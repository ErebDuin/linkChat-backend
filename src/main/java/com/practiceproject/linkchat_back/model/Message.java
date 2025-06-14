//package com.practiceproject.linkchat_back.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Table;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//@Table(name = "messages")
//public class Message {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    private long chatId;
//    private String messageText;
//    private String fromUser;
//    private String toUser;
//    private long timestamp;
//    private String messageType;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getChatId() {
//        return chatId;
//    }
//
//    public void setChatId(long chatId) {
//        this.chatId = chatId;
//    }
//
//    public String getMessageText() {
//        return messageText;
//    }
//
//    public void setMessageText(String messageText) {
//        this.messageText = messageText;
//    }
//
//    public String getFromUser() {
//        return fromUser;
//    }
//
//    public void setFromUser(String fromUser) {
//        this.fromUser = fromUser;
//    }
//
//    public String getToUser() {
//        return toUser;
//    }
//
//    public void setToUser(String toUser) {
//        this.toUser = toUser;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getMessageType() {
//        return messageType;
//    }
//
//    public void setMessageType(String messageType) {
//        this.messageType = messageType;
//    }
//}
