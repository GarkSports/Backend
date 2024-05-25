package com.gark.garksport.service;

import com.gark.garksport.dto.chat.ChatContactDTO;
import com.gark.garksport.dto.chat.ChatDTO;
import com.gark.garksport.modal.Chat;
import com.gark.garksport.modal.Equipe;
import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.User;
import com.gark.garksport.repository.ChatRepository;
import com.gark.garksport.repository.EquipeRepository;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    private final UserService userService;

    private final NotificationService notificationService;

    public List<ChatDTO> getChatHistory(Principal connectedUser, Integer userId2) {
        Integer userId1 = userService.getUserId(connectedUser.getName());
        User user1 = userRepository.findById(userId1).orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);
        List<Chat> chatList = chatRepository.findBySenderAndReceiversContainingOrReceiversContainingAndSenderOrderByTimestampDesc(user1, user2,user1, user2);
        List<ChatDTO> chatDTOList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setSenderId(chat.getSender().getId());
            chatDTO.setReceiversId(chat.getReceivers().stream().map(User::getId).collect(Collectors.toList()));
            chatDTO.setMessage(chat.getMessage());
            chatDTO.setTimestamp(chat.getTimestamp());
            chatDTOList.add(chatDTO);
        }
        return chatDTOList;
    }



    public ChatDTO sendMessage(Principal connectedUser, List<Integer> receiverdId,Integer idequipe, String message) {
        Chat chat = new Chat();
        Set<User> receivers = new HashSet<>();

        Integer senderId = userService.getUserId(connectedUser.getName());
        User sender = userRepository.findById(senderId).orElse(null);
        if ((idequipe != null && idequipe != 0) && (receiverdId != null && !receiverdId.isEmpty())) {
            throw new IllegalArgumentException("Both idEquipe and idMembres cannot be provided simultaneously.");
        }

        if (idequipe != null && idequipe != 0) {
            // Retrieve the Equipe from the repository using idEquipe
            Equipe equipe = equipeRepository.findById(idequipe)
                    .orElseThrow(() -> new NoSuchElementException("Equipe not found with id: " + idequipe));
            chat.setGroup(equipe);
            receivers = equipe.getAdherents().stream()
                    .map(adherent -> (User) adherent)
                    .collect(Collectors.toSet());
            chat.setReceivers(receivers);
            System.out.println("equipe id  ");
            System.out.println(receivers.size());






        } else if (receiverdId != null && !receiverdId.isEmpty()) {
            chat.setGroup(null);
            receivers = userRepository.findByIdIn(receiverdId);
            chat.setReceivers(receivers);

        }

        System.out.println("test receivers");
        System.out.println(receiverdId);
        System.out.println("test receivers");
        System.out.println(receivers.size());


        chat.setSender(sender);
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chatRepository.save(chat);




        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("vous avez recu un nouveau message ");
        notificationMessage.setImage("https://cdn.iconscout.com/icon/free/png-256/free-message-2367724-1976874.png?f=webp&w=256");
        for (User receiver : receivers) {
            notificationService.sendNotificationToUser(receiver.getId(), notificationMessage);
        }

        // Create and return the ChatMessageDTO
        ChatDTO chatMessageDTO = new ChatDTO();
        chatMessageDTO.setSenderId(senderId);
        chatMessageDTO.setReceiversId(receiverdId);
        chatMessageDTO.setMessage(message);
        chatMessageDTO.setTimestamp(LocalDateTime.now());
        return chatMessageDTO;
    }

    public ChatDTO sendGroupMessage(Principal connectedUser, Integer groupId, String message) {
        Integer senderId = userService.getUserId(connectedUser.getName());
        User sender = userRepository.findById(senderId).orElse(null);
        Equipe group = equipeRepository.findById(groupId).orElse(null);

        Set<User> receivers = new HashSet<>(group.getAdherents());

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceivers(receivers);
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chat.setGroup(group);
        chatRepository.save(chat);

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("GarkSport");
        notificationMessage.setBody("Nouveau message de groupe");
        notificationMessage.setImage("https://cdn.iconscout.com/icon/free/png-256/free-message-2367724-1976874.png?f=webp&w=256");

        for (User receiver : receivers) {
            notificationService.sendNotificationToUser(receiver.getId(), notificationMessage);
        }

        ChatDTO chatMessageDTO = new ChatDTO();
        chatMessageDTO.setSenderId(senderId);
        chatMessageDTO.setReceiversId(receivers.stream().map(User::getId).collect(Collectors.toList()));
        chatMessageDTO.setMessage(message);
        chatMessageDTO.setTimestamp(LocalDateTime.now());
        return chatMessageDTO;
    }

    public List<ChatContactDTO> getUsersWithMessages(Principal connectedUser) {
        Integer currentUserId = userService.getUserId(connectedUser.getName());
        User currentUser = userRepository.findById(currentUserId).orElse(null);

        List<ChatContactDTO> usersWithMessages = new ArrayList<>();
        List<Chat> chats = chatRepository.findBySenderOrReceiversContainingOrderByTimestampDesc(currentUser);
        for (Chat chat : chats) {
            if (!chat.getSender().equals(currentUser)) {
                usersWithMessages.add(convertToDTO(chat.getSender()));
            }
            for (User receiver : chat.getReceivers()) {
                if (!receiver.equals(currentUser)) {
                    usersWithMessages.add(convertToDTO(receiver));
                }
            }
        }

        return usersWithMessages.stream().distinct().collect(Collectors.toList());
    }

    private ChatContactDTO convertToDTO(User user) {
        ChatContactDTO chatUserDTO = new ChatContactDTO();
        chatUserDTO.setUserId(user.getId());
        chatUserDTO.setUsername(user.getFirstname() + " " + user.getLastname());
        return chatUserDTO;
    }

}
