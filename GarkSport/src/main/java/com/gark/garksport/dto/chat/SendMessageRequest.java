package com.gark.garksport.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class SendMessageRequest {
    private List<Integer> receiversId;
    private String message;
    private Integer idEquipe;


    // getters and setters
}
