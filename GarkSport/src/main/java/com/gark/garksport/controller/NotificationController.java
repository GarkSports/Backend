package com.gark.garksport.controller;


import com.gark.garksport.modal.NotificationToken;
import com.gark.garksport.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

//    @PostMapping("/codeequipe/{codeequipe}")
//    public void sendNotificaitonByToken(@RequestBody NotificationMessage notificationMessage, @PathVariable String codeequipe){
//        notificationService.sendNotificationToTeam(codeequipe, notificationMessage);
//    }
//
//    @PostMapping("/academieid/{academieid}")
//    public void sendNotificaitonByacademieid(@RequestBody NotificationMessage notificationMessage, @PathVariable Integer academieid){
//        notificationService.sendNotificationToAcademy(academieid, notificationMessage);
//    }


    @PostMapping("/addtoken")
    public ResponseEntity addNotificaitonToken(Principal currentUser, @RequestParam String token){
        if (currentUser == null || token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tous les champs doivent être remplis.");
        }


        NotificationToken creatednotiftoken = notificationService.addnotificationtoken(currentUser,token);
        if (creatednotiftoken != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(creatednotiftoken);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la création du post.");
        }
    }

    @DeleteMapping("/deletetoken")
    public void deleteNotificaitonToken(Principal currentUser){
        notificationService.deletetoken(currentUser);

    }
}
