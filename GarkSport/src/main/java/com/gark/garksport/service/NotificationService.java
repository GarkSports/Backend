package com.gark.garksport.service;

import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.NotificationToken;
import com.gark.garksport.repository.NotificationTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private NotificationTokenRepository notificationTokenRepository;


    public void addnotificationtoken(NotificationToken notificationToken){
        notificationTokenRepository.save(notificationToken);
    }

    public void sendNotificationToAcademy(Integer academieId, NotificationMessage notificationMessage) {
        List<String> tokens = notificationTokenRepository.findTokensByAcademieId(academieId);
        sendNotificationToTokens(tokens, notificationMessage);
    }

    public void sendNotificationToTeam(String codeEquipe, NotificationMessage notificationMessage) {
        List<String> tokens = notificationTokenRepository.findTokensByCodeEquipe(codeEquipe);
        sendNotificationToTokens(tokens, notificationMessage);
    }

    public void sendNotificationToUser(Integer userId, NotificationMessage notificationMessage) {
        NotificationToken notificationToken = notificationTokenRepository.findByUserId(userId);
        if (notificationToken != null) {
            String token = notificationToken.getToken();
            if (token != null) {
                List<String> tokens = new ArrayList<>();
                tokens.add(token);
                sendNotificationToTokens(tokens, notificationMessage);
            } else {
                // Handle the case where the token is null
                // For example, log an error or throw an exception
                System.err.println("Token is null for user: " + userId);
            }
        } else {
            // Handle the case where the NotificationToken is not found for the user
            // For example, log an error or throw an exception
            System.err.println("NotificationToken not found for user: " + userId);
        }
    }

    private String sendNotificationToTokens(List<String> tokens, NotificationMessage notificationMessage) {
        Notification notification =Notification
                .builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();
        for (String token : tokens) {
            Message message = Message
                    .builder()
                    .setToken(token)
                    .setNotification(notification)
                    //.putAllData(notificationMessage.getData())
                    .build();
            try{
                firebaseMessaging.send(message);
                return "Success sending notification";
            }catch(FirebaseMessagingException e){
                e.printStackTrace();
                return "Error Sendin Notification";
            }
        }
        return "end of sending notifications";
    }
}
