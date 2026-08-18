package com.family_management_system.master_service.controller;

import com.family_management_system.master_service.dto.ApiResponse;
import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.service.MasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/master")
public class MasterController {
    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @GetMapping("/qualifications")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getQualifications(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Qualifications fetched",
                HttpStatus.OK,
                masterService.getQualifications()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professions")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getProfessions(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Professions fetched",
                HttpStatus.OK,
                masterService.getProfessions()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/designations")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getDesignations(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Designations fetched",
                HttpStatus.OK,
                masterService.getDesignations()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/blood-groups")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getBloodGroups(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Blood Groups fetched",
                HttpStatus.OK,
                masterService.getBloodGroups()
        );
        return ResponseEntity.ok(response);
    }

}
