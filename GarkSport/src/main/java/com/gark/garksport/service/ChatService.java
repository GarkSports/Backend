package com.gark.garksport.service;

import com.gark.garksport.dto.chat.ChatContactDTO;
import com.gark.garksport.dto.chat.ChatDTO;
import com.gark.garksport.modal.Chat;
import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.ChatRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    private final UserService userService;

    private final NotificationService notificationService;

    public List<ChatDTO> getChatHistory(Principal connectedUser, Integer userId2) {
        Integer userId1 = userService.getUserId(connectedUser.getName());
        User user1 = userRepository.findById(userId1).orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);
        List<Chat> chatList = chatRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampDesc(user1, user2,user1,user2);
        List<ChatDTO> chatDTOList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setSenderId(chat.getSender().getId());
            chatDTO.setReceiverId(chat.getReceiver().getId());
            chatDTO.setMessage(chat.getMessage());
            chatDTO.setTimestamp(chat.getTimestamp());
            chatDTOList.add(chatDTO);
        }
        return chatDTOList;
    }



    public ChatDTO sendMessage(Principal connectedUser, User receiver, String message) {
        Integer senderId = userService.getUserId(connectedUser.getName());
        User sender = userRepository.findById(senderId).orElse(null);

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chatRepository.save(chat);

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez recu un nouveau message ");
        notificationMessage.setImage("https://cdn.iconscout.com/icon/free/png-256/free-message-2367724-1976874.png?f=webp&w=256");
        notificationService.sendNotificationToUser(
                receiver.getId(),
                notificationMessage
                );

        // Create and return the ChatMessageDTO
        ChatDTO chatMessageDTO = new ChatDTO();
        chatMessageDTO.setSenderId(senderId);
        chatMessageDTO.setReceiverId(receiver.getId());
        chatMessageDTO.setMessage(message);
        chatMessageDTO.setTimestamp(LocalDateTime.now());
        return chatMessageDTO;
    }

    public List<ChatContactDTO> getUsersWithMessages(Principal connectedUser) {
        Integer currentUserId = userService.getUserId(connectedUser.getName());
        User currentUser = userRepository.findById(currentUserId).orElse(null);

        List<ChatContactDTO> usersWithMessages = new ArrayList<>();
        List<Chat> chats = chatRepository.findBySenderOrReceiverOrderByTimestampDesc(currentUser, currentUser);
        for (Chat chat : chats) {
            if (!chat.getSender().equals(currentUser)) {
                usersWithMessages.add(convertToDTO(chat.getSender()));
            }
            if (!chat.getReceiver().equals(currentUser)) {
                usersWithMessages.add(convertToDTO(chat.getReceiver()));
            }
        }

        // Use distinct to remove duplicate users
        return usersWithMessages.stream().distinct().collect(Collectors.toList());
    }

    // Method to convert User entity to ChatContactDTO
    private ChatContactDTO convertToDTO(User user) {
        ChatContactDTO chatUserDTO = new ChatContactDTO();
        chatUserDTO.setUserId(user.getId());
        chatUserDTO.setUsername(user.getFirstname() + " " + user.getLastname());
        // Add other fields as needed
        return chatUserDTO;
    }

}
