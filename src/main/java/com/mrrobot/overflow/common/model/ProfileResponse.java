package com.mrrobot.overflow.common.model;

import com.mrrobot.overflow.post.model.PostResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProfileResponse {

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String addressLine;
    private String city;
    private String phoneNo;
    private Integer level;
    private String status;
    private Double point;
    private Integer position;
    private Boolean phoneNoVisibility;
    private Boolean isOpenForJob;
    private Date createdDate;
    private Date updatedDate;

    private long totalPost;
    private long totalGroup;
    private long totalComment;
    private long totalLike;

    private List<PostResponse> posts;

    public ProfileResponse() {
    }

    public ProfileResponse(String username, String email, String firstName, String lastName, String addressLine, String city, String phoneNo, Integer level, Double point, Boolean isOpenForJob, Date createdDate, Date updatedDate) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine = addressLine;
        this.city = city;
        this.phoneNo = phoneNo;
        this.level = level;
        this.point = point;
        this.isOpenForJob = isOpenForJob;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
