package com.gark.garksport.service;

import com.gark.garksport.dto.chat.ChatContactDTO;
import com.gark.garksport.dto.chat.ChatDTO;

import java.security.Principal;
import java.util.List;

public interface IChatService {


    public List<ChatDTO> getChatHistory(Principal connectedUser, Integer userId2);

    public ChatDTO sendMessage(Principal connectedUser, List<Integer> receiverdId,Integer idequipe, String message);

    public List<ChatContactDTO> getUsersWithMessages(Principal connectedUser);


    public void removeUserFromDiscussions(Principal principal, Integer otherUserId);
}
