package com.mrrobot.overflow.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    Long id;
    String description;
    Integer hit;
    Double point;
    Date createdDate;
    Date updatedDate;
    String commentedUser;
}
