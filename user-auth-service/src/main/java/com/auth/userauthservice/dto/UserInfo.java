package com.auth.userauthservice.dto;

import java.util.List;

import lombok.Value;

@Value
public class UserInfo {
    private String id, displayName, email;
    private List<String> roles;
}