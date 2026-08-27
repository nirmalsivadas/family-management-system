package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.service.FamilyService;
import com.family_management_system.family_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping("/view-families")
    public ResponseEntity<ApiResponse<Page<ViewFamilies>>> viewFamilies(
            @RequestParam Long userId,
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

    @GetMapping("/view-members")
    public ResponseEntity<ApiResponse<Page<ViewMembers>>> viewMembers(
            @RequestParam Long userId,
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

    @GetMapping("/{memberShipId}/view-family")
    public ResponseEntity<ApiResponse<ViewFamily>> viewFamily(
            @RequestParam Long userId,
            @PathVariable String memberShipId){
        ApiResponse<ViewFamily> response = new ApiResponse<>(
                "fetched family",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.viewFamily(userId,memberShipId)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register-family",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<RegisterResponse>> registerFamily(
            @Valid @RequestPart("request") RegisterFamilyRequest registerFamilyRequest,
            @RequestPart(value = "photo") MultipartFile photo
            ) throws IOException {
        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                "family registration successful",
                HttpStatus.CREATED,
                LocalDateTime.now(),
                familyService.registerFamily(registerFamilyRequest,photo)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent-families")
    public ResponseEntity<ApiResponse<List<RecentFamilies>>> recentFamilies(@RequestParam Long userId){
        ApiResponse<List<RecentFamilies>> response = new ApiResponse<>(
                "recent families registered fetched",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.recentFamilies(userId)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-family")
    public ResponseEntity<ApiResponse<String>> updateFamily(@RequestParam Long userId,
            @Valid @RequestBody UpdateFamilyRequest updateFamilyRequest
    ){
        ApiResponse<String> response = new ApiResponse<>(
                "family updated",
                HttpStatus.OK,
                LocalDateTime.now(),
                familyService.updateFamily(userId,updateFamilyRequest)
        );
        return ResponseEntity.ok(response);
    }
}
