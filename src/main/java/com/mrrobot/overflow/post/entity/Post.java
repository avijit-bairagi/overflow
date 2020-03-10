package com.mrrobot.overflow.post.entity;

import com.mrrobot.overflow.profile.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long groupId = 0l;
    String title;
    String description;
    Integer hit = 0;
    Integer favourite = 0;
    Double point = 0.0;
    Long postedBy;
    Date createdDate = new Date();
    Date updatedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_topics",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics = new HashSet<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "post_likes",
//            joinColumns = @JoinColumn(name = "post_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Set<User> users = new HashSet<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "post_comments",
//            joinColumns = @JoinColumn(name = "post_id"),
//            inverseJoinColumns = @JoinColumn(name = "comment_id"))
//    private Set<Comment> comments = new HashSet<>();

    public Post() {
    }

    public Post(String title, String description, Long postedBy, Set<Topic> topics) {
        this.title = title;
        this.description = description;
        this.postedBy = postedBy;
        this.topics = topics;
    }
}