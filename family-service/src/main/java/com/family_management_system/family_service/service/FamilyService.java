package com.family_management_system.family_service.service;

import com.family_management_system.family_service.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface FamilyService {

    RegisterResponse registerFamily(RegisterFamilyRequest registerFamilyRequest);
    Page<ViewFamilies> viewFamilies(Long userId, String status,
                                    int page, int size);
    Page<ViewMembers> viewMembers(Long userId,int page,int size);
    ViewFamily viewFamily(Long userId,String memberShipId);
    String updateFamily(@Valid UpdateFamilyRequest updateFamilyRequest);

}
