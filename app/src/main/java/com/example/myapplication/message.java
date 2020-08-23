package com.example.myapplication;

public class message {
    String message,text,seen,from;
    long timestamp;

    public message() {
    }

    public message(String message, String text, String seen, long timestamp,String from) {
        this.message = message;
        this.text = text;
        this.seen = seen;
        this.timestamp = timestamp;
        this.from=from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
