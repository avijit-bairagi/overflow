package com.mrrobot.overflow.post.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostBody {

    private String title;
    private String description;
    private Set<String> topics;
}
