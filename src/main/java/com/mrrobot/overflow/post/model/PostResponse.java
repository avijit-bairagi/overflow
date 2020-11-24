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

    Long id;
    String title;
    String description;
    Integer hit;
    Double point;
    Long postedBy;
    Long groupId;
    Date createdDate;
    Date updatedDate;
    Integer totalComments;
    Integer totalLikes;

    Set<Topic> topics;
    List<CommentResponse> comments;
    List<Like> likes;
}
