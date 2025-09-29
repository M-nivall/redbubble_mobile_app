package com.example.Varsani.Clients.Models;

public class FeedbackModel {
    String comment;
    String reply;
    String recipient;
    String sender;

    public FeedbackModel(String comment, String reply, String recipient, String sender){
        this.comment=comment;
        this.recipient=recipient;
        this.sender=sender;
        this.reply=reply;
    }

    public String getComment() {
        return comment;
    }
    public String getRecipient(){
        return recipient;
    }
    public String getSender(){
        return sender;
    }

    public String getReply() {
        return reply;
    }
}
