package com.gark.garksport.dto.chat;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Integer receiverId;
    private String message;

    // getters and setters
}
