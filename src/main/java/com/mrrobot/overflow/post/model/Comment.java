package com.mrrobot.overflow.post.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long postId;
    String description;
    Integer hit;
    Long commentedBy;
    Date createdDate;
    Date updatedDate;
}