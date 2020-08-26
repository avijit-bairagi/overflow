package com.mrrobot.overflow.profile.entity;

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
    Integer level = 1;
    Double point = 0.0;
    Boolean isOpenForJob = false;
    Boolean phoneNoVisibility = true;
    Boolean isActive = true;
    Date createdDate = new Date();
    Date updatedDate;

    public Profile() {
    }

    public Profile(String firstName, String lastName, String addressLine, String city, String phoneNo, Boolean isOpenForJob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine = addressLine;
        this.city = city;
        this.phoneNo = phoneNo;
        this.isOpenForJob = isOpenForJob;
    }
}