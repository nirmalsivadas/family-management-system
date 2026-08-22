package com.family_management_system.family_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewProfile {
    private String userName;
    private String userEmail;
    private String createdAt;
    private String lastUpdatedAt;
    private String mobileNumber;

}
