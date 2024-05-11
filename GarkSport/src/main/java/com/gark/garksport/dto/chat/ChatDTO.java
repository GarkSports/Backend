package com.gark.garksport.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ChatDTO {
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private LocalDateTime timestamp;

    // Getters and setters
}
