package com.mrrobot.overflow.common.model;

import com.mrrobot.overflow.common.entity.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigResponse {

    Config config;
    long totalUser;
    long totalPost;
    long totalGroup;
}
