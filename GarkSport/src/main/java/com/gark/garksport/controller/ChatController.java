package com.gark.garksport.controller;

import com.gark.garksport.dto.chat.ChatContactDTO;
import com.gark.garksport.dto.chat.ChatDTO;
import com.gark.garksport.dto.chat.SendMessageRequest;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/history")
    public ResponseEntity<List<ChatDTO>> getChatHistory(Principal currentUser, @RequestParam Integer userId2) {
        if (currentUser == null || userId2 == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatDTO> chatHistory = chatService.getChatHistory(currentUser, userId2);
        return ResponseEntity.ok(chatHistory);
    }

    @PostMapping("/send")
    public ResponseEntity<ChatDTO> sendMessage(Principal currentUser, @RequestBody SendMessageRequest request) {
        List<Integer> receiversId = request.getReceiversId();
        String message = request.getMessage();
        Integer idEquipe = request.getIdEquipe();

        if (currentUser == null || message == null || (receiversId == null && idEquipe == null) || (receiversId != null && receiversId.isEmpty())) {
            return ResponseEntity.badRequest().build();
        } else
        if (idEquipe != null) {
            System.out.println("with id_equipe");
            System.out.println(idEquipe);
            ChatDTO chatMessageDTO = chatService.sendMessage(currentUser, null,idEquipe, message);
            return ResponseEntity.ok(chatMessageDTO);

        } else  if (receiversId != null && !receiversId.isEmpty()) {
            System.out.println("with receivers");
            System.out.println(receiversId);
            ChatDTO chatMessageDTO = chatService.sendMessage(currentUser, receiversId, null ,message);
            return ResponseEntity.ok(chatMessageDTO);

        } throw new IllegalArgumentException("Either idEquipe or receiversId must be provided.");

    }

    @GetMapping("/usersWithMessages")
    public ResponseEntity<List<ChatContactDTO>> getUsersWithMessages(Principal currentUser) {

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatContactDTO> usersWithMessages = chatService.getUsersWithMessages(currentUser);
        return ResponseEntity.ok(usersWithMessages);
    }
}
