package com.mrrobot.overflow.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrrobot.overflow.profile.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "pgroups")
public class Group {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Long createdBy;
    Date createdDate = new Date();
    Date updatedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pgroup_member",
            joinColumns = @JoinColumn(name = "pgroup_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();
}