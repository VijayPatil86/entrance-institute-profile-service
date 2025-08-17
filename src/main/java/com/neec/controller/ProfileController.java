package com.neec.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neec.dto.CustomPrincipal;
import com.neec.dto.ProfileRequestDTO;
import com.neec.dto.ProfileResponseDTO;
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
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity<?> saveProfile(@Valid @RequestBody ProfileRequestDTO dto, @AuthenticationPrincipal CustomPrincipal customPrincipal){
		Long userId = Long.valueOf(customPrincipal.getSubject());
		profileService.saveProfile(userId, dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "Student Profile is created"));
	}

	@GetMapping("/me")
	ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomPrincipal customPrincipal){
		Long userId = Long.valueOf(customPrincipal.getSubject());
		ProfileResponseDTO profileResponseDTO = profileService.getProfileByUserId(userId);
		return ResponseEntity.ok(profileResponseDTO);
	}

	@PutMapping("/me")
	ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequestDTO dto, @AuthenticationPrincipal CustomPrincipal customPrincipal){
		Long userId = Long.valueOf(customPrincipal.getSubject());
		profileService.updateProfile(userId, dto);
		return ResponseEntity.ok(Map.of("status", "Student Profile is updated"));
	}
}
