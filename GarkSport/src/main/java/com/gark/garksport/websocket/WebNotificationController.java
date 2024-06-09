package com.gark.garksport.websocket;

import com.gark.garksport.modal.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebNotificationController {
    @Autowired
    private SimpMessagingTemplate template;
    // Initialize Notifications

    public String sendNotification(String user,NotificationMessage NotificationMessage) {




        template.convertAndSend("/queue/reply/"+user, NotificationMessage);
        return "Notifications successfully sent to Angular !";
    }

}
