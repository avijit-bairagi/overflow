package com.mrrobot.overflow.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    boolean isSeen = false;

    Long userId;

    Long postId;

    Long groupId;

    boolean isGroup;

    String text;

    Date createdTime = new Date();

}
