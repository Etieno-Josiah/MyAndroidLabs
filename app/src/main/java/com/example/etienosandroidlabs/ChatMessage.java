package com.example.etienosandroidlabs;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    protected boolean sendOrReceive;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public ChatMessage(String m, String t, boolean sent){
        message = m;
        timeSent = t;
        sendOrReceive = sent;
    }
    public ChatMessage(){}

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean getIsSentButton(){
        return sendOrReceive;
    }

}
