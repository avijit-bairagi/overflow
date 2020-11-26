package com.mrrobot.overflow.common.model;

import com.mrrobot.overflow.post.entity.Topic;
import com.mrrobot.overflow.post.model.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRes {

    List<PostResponse> posts;

    List<ProfileResponse>  users;
    List<Topic> topics;
}
