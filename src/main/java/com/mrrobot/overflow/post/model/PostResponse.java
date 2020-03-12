package com.mrrobot.overflow.post.model;

import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.Like;
import com.mrrobot.overflow.post.entity.Topic;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostResponse {

    String title;
    String description;
    Integer hit;
    Integer favourite;
    Double point;
    Long postedBy;
    Long groupId;
    Date createdDate;
    Date updatedDate;

    Set<Topic> topics;
    List<Comment> comments;
    List<Like> likes;
}
