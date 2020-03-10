package com.mrrobot.overflow.post.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long likedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="postId", nullable=false)
    private Post post;
}