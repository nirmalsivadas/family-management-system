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

    @GetMapping("/states")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getStates(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "States fetched",
                HttpStatus.OK,
                masterService.getStates()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getCountries(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Countries fetched",
                HttpStatus.OK,
                masterService.getCountries()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/genders")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getGenders(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Genders fetched",
                HttpStatus.OK,
                masterService.getGenders()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/occupations")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getOccupations(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Occupations fetched",
                HttpStatus.OK,
                masterService.getOccupations()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/marital-status")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getMaritalStatus(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Marital Status fetched",
                HttpStatus.OK,
                masterService.getMaritalStatus()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/membership-types")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getMemberShipTypes(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Membership Types fetched",
                HttpStatus.OK,
                masterService.getMemberShipTypes()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/registration-categories")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getRegistrationCategories(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Registration categories fetched",
                HttpStatus.OK,
                masterService.getRegistrationCategories()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getCities(){
        ApiResponse<List<MasterResponse>> response = new ApiResponse<>(
                "Cities fetched",
                HttpStatus.OK,
                masterService.getCities()
        );
        return ResponseEntity.ok(response);
    }

}
