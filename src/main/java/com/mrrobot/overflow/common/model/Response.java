package com.mrrobot.overflow.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    String code;
    String message;
    Object data;
}
