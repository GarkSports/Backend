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
        Integer receiverId = request.getReceiverId();
        String message = request.getMessage();
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (currentUser == null || receiver == null || message == null) {
            return ResponseEntity.badRequest().build();
        }
        ChatDTO chatMessageDTO = chatService.sendMessage(currentUser, receiver, message);
        return ResponseEntity.ok(chatMessageDTO);
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
