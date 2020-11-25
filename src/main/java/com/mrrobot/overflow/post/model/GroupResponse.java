package com.mrrobot.overflow.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {

    Long id;
    String name;
    String description;
    Date createdDate;
    Date updatedDate;
    String createdByUser;
    int totalPost;
    int totalMember;

    List<PostResponse> posts;

    List<KeyValue> allUsers;
    List<KeyValue> users;
}
