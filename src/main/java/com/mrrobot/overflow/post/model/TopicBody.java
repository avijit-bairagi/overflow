package com.mrrobot.overflow.post.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicBody {

    private String name;
    private Long createdBy;

    public TopicBody() {
    }

    public TopicBody(String name) {
        this.name = name;
    }
}
