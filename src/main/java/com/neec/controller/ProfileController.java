package com.neec.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neec.dto.CustomPrincipal;
import com.neec.dto.ProfileRequestDTO;
import com.neec.service.ProfileService;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/profiles")
public class ProfileController {
	private ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@Observed(contextualName = "spanProfileControllerSaveProfile")
	@PostMapping
	ResponseEntity<?> saveProfile(@Valid @RequestBody ProfileRequestDTO dto, @AuthenticationPrincipal CustomPrincipal customPrincipal){
		Long userId = Long.valueOf(customPrincipal.getSubject());
		profileService.saveProfile(userId, dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "Student Profile is created"));
	}
}
