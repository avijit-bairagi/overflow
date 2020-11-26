package com.mrrobot.overflow.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String appName;

    String nameEn;

    String nameBn;

    String type;

    String addressLine;

    String city;

    String logo;

    String domain;

    String apiEndPoint;

    String phoneNo;

    String email;

    String fax;

    boolean isApprovalNeeded;
}
