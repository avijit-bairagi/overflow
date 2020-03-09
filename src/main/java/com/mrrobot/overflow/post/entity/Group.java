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
@Entity(name = "rooms")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Long createdBy;
    Date createdDate;
    Date updatedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_members",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();
}