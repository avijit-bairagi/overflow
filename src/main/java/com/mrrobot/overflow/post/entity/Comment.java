package com.mrrobot.overflow.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    Integer hit = 0;
    Double point = 0.0;
    Long commentedBy;
    Date createdDate = new Date();
    Date updatedDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;
}