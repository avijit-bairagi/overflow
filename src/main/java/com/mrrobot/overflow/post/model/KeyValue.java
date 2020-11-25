package com.mrrobot.overflow.post.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValue {
    Long key;
    String value;

    public KeyValue() {
    }

    public KeyValue(Long key, String value) {
        this.key = key;
        this.value = value;
    }
}
