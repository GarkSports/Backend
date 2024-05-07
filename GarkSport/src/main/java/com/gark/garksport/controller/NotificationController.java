package com.gark.garksport.controller;


import com.gark.garksport.modal.NotificationMessage;
import com.gark.garksport.modal.NotificationToken;
import com.gark.garksport.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

//    @Autowired
//    FirebaseMessagingService firebaseMessagingService;
//
//    @PostMapping
//    public String sendNotificaitonByToken(@RequestBody NotificationMessage notificationMessage){
//        return firebaseMessagingService.sendNotificationByToken(notificationMessage);
//    }

    @Autowired
    NotificationService notificationService;

    @PostMapping("/codeequipe/{codeequipe}")
    public void sendNotificaitonByToken(@RequestBody NotificationMessage notificationMessage, @PathVariable String codeequipe){
        notificationService.sendNotificationToTeam(codeequipe, notificationMessage);
    }

    @PostMapping("/academieid/{academieid}")
    public void sendNotificaitonByacademieid(@RequestBody NotificationMessage notificationMessage, @PathVariable Integer academieid){
        notificationService.sendNotificationToAcademy(academieid, notificationMessage);
    }


    @PostMapping("/addtoken")
    public void addNotificaitonToken(@RequestBody NotificationToken notifToken){
        NotificationToken notificationToken = new NotificationToken();
        notificationToken.setUserId(notifToken.getUserId());
        notificationToken.setToken(notifToken.getToken());
        notificationToken.setCodeEquipe(notifToken.getCodeEquipe());
        notificationToken.setAcademieId(notifToken.getAcademieId());

        notificationService.addnotificationtoken(notificationToken);
    }
}
