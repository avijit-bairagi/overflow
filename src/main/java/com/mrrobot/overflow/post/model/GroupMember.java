package com.mrrobot.overflow.post.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "groupmembers")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long groupId;
    Long userId;
}