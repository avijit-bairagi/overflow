package com.mrrobot.overflow.common.model;

import com.mrrobot.overflow.post.model.GroupResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRes {

    List<GroupResponse> groups;

    long totalPost;

    long totalGroup;

    long totalPgroup;
}
