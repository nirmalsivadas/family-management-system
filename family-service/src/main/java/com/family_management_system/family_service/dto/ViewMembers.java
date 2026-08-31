package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewMembers {
    private String rowId;
    private String name;
    private String relationShip;
    private String familyName;
    private String memberShipId;
    private String occupation;
    private Long mobileNumber;
    private Status status;

}
