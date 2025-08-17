package com.neec.service;

import com.neec.dto.ProfileRequestDTO;
import com.neec.dto.ProfileResponseDTO;

public interface ProfileService {
	void saveProfile(long userId, ProfileRequestDTO dto);

	ProfileResponseDTO getProfileByUserId(Long userId);
}
