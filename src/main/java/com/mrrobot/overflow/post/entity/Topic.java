package com.mrrobot.overflow.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "topics")
public class Topic {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @JsonIgnore
    Integer hit = 0;
    @JsonIgnore
    Date createdDate = new Date();
    @JsonIgnore
    Long createdBy;

    public Topic() {
    }

    public Topic(String name, Long createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}