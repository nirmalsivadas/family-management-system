package com.family_management_system.family_service.service;

import com.family_management_system.family_service.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FamilyService {

    RegisterResponse registerFamily(RegisterFamilyRequest registerFamilyRequest
    , MultipartFile photo) throws IOException;
    Page<ViewFamilies> viewFamilies(Long userId, String status,
                                    int page, int size, String query);
    Page<ViewMembers> viewMembers(Long userId,int page,int size, String query);
    ViewFamily viewFamily(Long userId,String memberShipId);
    String updateFamily(Long userId, UpdateFamilyRequest updateFamilyRequest);
    String changeStatus(Long userId, String memberShipId, String status);
    List<RecentFamilies> recentFamilies(Long userId);
}
