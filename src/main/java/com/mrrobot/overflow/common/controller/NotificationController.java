package com.mrrobot.overflow.common.controller;

import com.mrrobot.overflow.common.entity.Notification;
import com.mrrobot.overflow.common.model.NotificationResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.repository.NotificationRepository;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("notification/")
public class NotificationController {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Response> getNotification() {

        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Notification fetched successfully.");

        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedTimeDesc(userService.getUserData().getUserId());

        NotificationResponse notificationResponse = new NotificationResponse();

        notificationResponse.setNotifications(notifications);
        notificationResponse.setTotalUnseen(notifications.stream().filter(notification -> !notification.isSeen()).count());

        response.setData(notificationResponse);

        return ResponseEntity.ok().body(response);
    }

}
