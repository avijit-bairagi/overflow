package com.mrrobot.overflow.profile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordBody {

    private String oldPassword;
    private String newPassword;
}