package com.mrrobot.overflow.common.model;

import com.mrrobot.overflow.common.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    List<Notification> notifications;
    long totalUnseen;
}
