package com.gark.garksport.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatDTO {
    private Integer senderId;
    private List<Integer> receiversId;
    private String message;
    private LocalDateTime timestamp;

    // Getters and setters
}
