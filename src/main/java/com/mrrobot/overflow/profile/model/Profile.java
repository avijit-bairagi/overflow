package com.mrrobot.overflow.profile.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long userId;
    String firstName;
    String lastName;
    String addressLine;
    String city;
    String phoneNo;
    Integer level;
    Double point;
    Boolean isOpenForJob;
    Date createdDate;
    Date updatedDate;
}