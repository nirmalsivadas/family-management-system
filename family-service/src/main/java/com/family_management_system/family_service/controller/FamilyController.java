//package com.family_management_system.family_service.controller;
//
//import com.family_management_system.family_service.dto.ApiResponse;
//import com.family_management_system.family_service.dto.RegisterFamilyRequest;
//import com.family_management_system.family_service.dto.UpdateProfileRequest;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/family")
//public class FamilyController {
//    @GetMapping("/view-families")
//    public ResponseEntity<ApiResponse> viewFamilies(){
//        ApiResponse<> response = new ApiResponse<>();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/view-members")
//    public ResponseEntity<ApiResponse> viewMembers(){
//        ApiResponse<> response = new ApiResponse<>();
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/register-family")
//    public ResponseEntity<ApiResponse> registerFamily(
//            @Valid @ModelAttribute RegisterFamilyRequest registerFamilyRequest
//            ){
//        ApiResponse<> response = new ApiResponse<>();
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/update-profile")
//    public ResponseEntity<ApiResponse> updateProfile(
//            @Valid @RequestBody UpdateProfileRequest updateProfileRequest
//    ){
//        ApiResponse<> response = new ApiResponse<>();
//        return ResponseEntity.ok(response);
//    }
//}
