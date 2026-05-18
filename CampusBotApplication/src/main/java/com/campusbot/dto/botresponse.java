package com.campusbot.dto;

import java.time.LocalDateTime;

public class botresponse {
    private String answer;
    private String status;
    private String timestamp;

    public botresponse(String answer, String status) {
        this.answer = answer;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getAnswer() 
    { 
    	return answer; 
    	}
    public void setAnswer(String answer) { this.answer = answer; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}