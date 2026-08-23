package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.service.FamilyService;
import com.family_management_system.family_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping("{userId}/view-families")
    public ResponseEntity<ApiResponse<Page<ViewFamilies>>> viewFamilies(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ApiResponse<Page<ViewFamilies>> response = new ApiResponse<>(
                "fetched families",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.viewFamilies(userId,status,page,size)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("{userId}/view-members")
    public ResponseEntity<ApiResponse<Page<ViewMembers>>> viewMembers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size){
        ApiResponse<Page<ViewMembers>> response = new ApiResponse<>(
                "fetched members",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.viewMembers(userId,page,size)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("{userId}/{memberShipId}/view-family")
    public ResponseEntity<ApiResponse<ViewFamily>> viewFamily(
            @PathVariable Long userId,
            @PathVariable String memberShipId){
        ApiResponse<ViewFamily> response = new ApiResponse<>(
                "fetched family",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.viewFamily(userId,memberShipId)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-family")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerFamily(
            @Valid @ModelAttribute RegisterFamilyRequest registerFamilyRequest
            ){
        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                "family registration successful",
                HttpStatus.CREATED,
                LocalDateTime.now(),
                familyService.registerFamily(registerFamilyRequest)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-family")
    public ResponseEntity<ApiResponse<String>> updateFamily(
            @Valid @RequestBody UpdateFamilyRequest updateFamilyRequest
    ){
        ApiResponse<String> response = new ApiResponse<>(
                "family updated",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.updateFamily(updateFamilyRequest)
        );
        return ResponseEntity.ok(response);
    }
}
