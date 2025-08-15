package com.neec.service;

import com.neec.dto.ProfileRequestDTO;
import com.neec.entity.StudentProfile;
import com.neec.entity.UserLogin;
import com.neec.repository.StudentProfileRepository;

public class ProfileServiceImpl implements ProfileService {
	private StudentProfileRepository studentProfileRepository;

	public ProfileServiceImpl(StudentProfileRepository studentProfileRepository) {
		this.studentProfileRepository = studentProfileRepository;
	}

	@Override
	public void createStudentProfile(long userId, ProfileRequestDTO dto) {
		if(studentProfileRepository.existsById(userId)) {
			throw new IllegalArgumentException("Student profile with id " + userId + " already exists.");
		}
		UserLogin userLogin = UserLogin.builder()
				.userLoginId(userId)
				.build();
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
