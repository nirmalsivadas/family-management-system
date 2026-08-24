package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.MasterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "master-service",path = "/api/master")
public interface MasterClient {

    @GetMapping("/professions")
    MasterDto getProfessions();

    @GetMapping("/qualifications")
    MasterDto getQualifications();

    @GetMapping("/designations")
    MasterDto getDesignations();

    @GetMapping("/blood-groups")
    MasterDto getBloodGroups();

}
