package com.gark.garksport.service;

import com.gark.garksport.modal.Adherent;
import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.NotificationToken;
import com.gark.garksport.repository.AdherentRepository;
import com.gark.garksport.repository.NotificationTokenRepository;
import com.gark.garksport.websocket.WebNotificationController;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private NotificationTokenRepository notificationTokenRepository;

    @Autowired
    private WebNotificationController webNotificationController;



    private final UserService userService;


    public NotificationToken addnotificationtoken(Principal currentUser, String token){
        Integer currentuserid = userService.getUserId(currentUser.getName());
        NotificationToken notificationdata = new NotificationToken();
        notificationdata.setUserId(currentuserid);
        notificationdata.setToken(token);
        notificationdata.setAcademieId(userService.getadherentacademieid(currentuserid));
        notificationdata.setCodeEquipe(userService.getadherentequipe(currentuserid));
        return notificationTokenRepository.save(notificationdata);
    }

    public void deletetoken(Principal currentUser){
        Integer currentuserid = userService.getUserId(currentUser.getName());
        notificationTokenRepository.deleteById(currentuserid);
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
        webNotificationController.getNotification();
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
            }catch(FirebaseMessagingException ex){
                if (ex.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    System.err.println("Device token has been unregistered");
                } else {
                    System.err.println("Failed to send the notification");
                }
            }
        }
        return "end of sending notifications";
    }
}
