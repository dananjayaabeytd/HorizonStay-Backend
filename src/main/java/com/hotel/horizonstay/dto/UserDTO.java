package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotel.horizonstay.entity.SystemUser;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private int statusCode;
    private String error;
    private String message;

    private int userId;
    private String token;
    private String refreshToken;
    private String expirationTime;

    private String email;
    private String name;
    private String password;
    private String address;
    private String role;
    private String image;
    private String NIC;

    private SystemUser systemUsers;
    private List<SystemUser> systemUsersList;

}
