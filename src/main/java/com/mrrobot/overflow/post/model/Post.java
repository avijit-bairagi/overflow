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
@Entity(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long groupId;
    String title;
    String description;
    Integer hit;
    Integer favourite;
    Double point;
    Long postedBy;
    Date createdDate;
    Date updatedDate;
}