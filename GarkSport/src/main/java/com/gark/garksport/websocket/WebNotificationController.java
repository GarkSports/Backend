package com.gark.garksport.websocket;

import com.gark.garksport.modal.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebNotificationController {
    @Autowired
    private SimpMessagingTemplate template;
    // Initialize Notifications
    private NotificationMessage notifications = new NotificationMessage();
    @GetMapping("/notify")
    public String getNotification() {
        // Increment Notification by one
        //notifications.increment();
        System.out.println("tstsetst");
        notifications.setTitle("ssss");
        notifications.setBody("qsqsqsqs");
        String user = "haythemdaoud99@gmail.com";
        // Push notifications to front-end
        template.convertAndSend("/queue/reply/haythemdaoud99@gmail.com", notifications);
        return "Notifications successfully sent to Angular !";
    }

}
