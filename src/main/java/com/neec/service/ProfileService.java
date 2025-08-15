package com.neec.service;

import com.neec.dto.ProfileRequestDTO;

public interface ProfileService {
	void createStudentProfile(long userId, ProfileRequestDTO dto);
}
