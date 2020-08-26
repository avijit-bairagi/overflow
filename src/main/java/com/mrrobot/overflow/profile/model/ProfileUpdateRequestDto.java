package com.mrrobot.overflow.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ProfileUpdateRequestDto {

    @NotNull
    Long userId;

    @NotBlank
    @Size(min = 3, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 40)

    private String addressLine;
    @NotBlank

    @Size(min = 3, max = 40)
    private String city;

    @NotBlank
    @Size(min = 6, max = 40)
    private String phoneNo;

    @NotNull
    private Boolean isOpenForJob;

    @NotNull
    private Boolean phoneNoVisibility;
}
