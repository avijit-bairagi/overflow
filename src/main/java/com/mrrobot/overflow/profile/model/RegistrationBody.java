package com.mrrobot.overflow.profile.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class RegistrationBody {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

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

    Boolean isOpenForJob;
}