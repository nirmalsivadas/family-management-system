package com.family_management_system.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private String firstName;
  private String lastName;
  private String mobileNumber;
  private String email;
  private String password;
}
