package com.neec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neec.dto.ProfileRequestDTO;
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
}
