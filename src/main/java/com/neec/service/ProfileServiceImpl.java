package com.neec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neec.dto.ProfileRequestDTO;
import com.neec.dto.ProfileResponseDTO;
import com.neec.entity.StudentProfile;
import com.neec.entity.UserLogin;
import com.neec.repository.StudentProfileRepository;

import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.EntityManager;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {
	private EntityManager entityManager;
	private StudentProfileRepository studentProfileRepository;

	public ProfileServiceImpl(StudentProfileRepository studentProfileRepository,
			EntityManager entityManager) {
		this.studentProfileRepository = studentProfileRepository;
		this.entityManager = entityManager;
	}

	@Observed(contextualName = "spanProfileServiceSaveProfile")
	@Override
	public void saveProfile(long userId, ProfileRequestDTO dto) {
		if(studentProfileRepository.existsById(userId)) {
			throw new IllegalArgumentException("Student profile with id " + userId + " already exists.");
		}
		UserLogin userLogin = entityManager.getReference(UserLogin.class, userId);

		StudentProfile studentProfile = StudentProfile.builder()
				.userLogin(userLogin)
				.firstName(dto.getFirstName())
				.lastName(dto.getLastName())
				.dateOfBirth(dto.getDateOfBirth())
				.gender(dto.getGender())
				.phoneNumber(dto.getPhoneNumber())
				.addressLine1(dto.getAddressLine1())
				.addressLine2(dto.getAddressLine2())
				.city(dto.getCity())
				.state(dto.getState())
				.pinCode(dto.getPinCode())
				.schoolName(dto.getSchoolName())
				.boardName(dto.getBoardName())
				.yearOfPassing(dto.getYearOfPassing())
				.percentage(dto.getPercentage())
				.build();
		StudentProfile savedStudentProfile = studentProfileRepository.save(studentProfile);
	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDTO getProfileByUserId(Long userId) {
		StudentProfile studentProfile = studentProfileRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Student profile with id " + userId + " does not exist."));
		ProfileResponseDTO profileDTO = ProfileResponseDTO.builder()
				.studentProfileId(studentProfile.getStudentProfileId())
				.email(studentProfile.getUserLogin().getEmailAddress())
				.firstName(studentProfile.getFirstName())
				.lastName(studentProfile.getLastName())
				.dateOfBirth(studentProfile.getDateOfBirth())
				.gender(studentProfile.getGender())
				.phoneNumber(studentProfile.getPhoneNumber())
				.addressLine1(studentProfile.getAddressLine1())
				.addressLine2(studentProfile.getAddressLine2())
				.city(studentProfile.getCity())
				.state(studentProfile.getState())
				.pinCode(studentProfile.getPinCode())
				.schoolName(studentProfile.getSchoolName())
				.boardName(studentProfile.getBoardName())
				.yearOfPassing(studentProfile.getYearOfPassing())
				.percentage(studentProfile.getPercentage())
				.build();
		return profileDTO;
	}
}
