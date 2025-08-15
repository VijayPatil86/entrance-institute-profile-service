package com.neec.service;

import com.neec.dto.ProfileRequestDTO;

public interface ProfileService {
	void saveProfile(long userId, ProfileRequestDTO dto);
}
