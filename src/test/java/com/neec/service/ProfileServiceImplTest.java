package com.neec.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neec.dto.ProfileRequestDTO;
import com.neec.entity.StudentProfile;
import com.neec.entity.UserLogin;
import com.neec.enums.EnumGender;
import com.neec.repository.StudentProfileRepository;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
	@Mock
	private StudentProfileRepository mockStudentProfileRepository;
	@InjectMocks
	private ProfileServiceImpl profileServiceImpl;

	@Test
	void test_createStudentProfile_ExistingProfile_Raise_IllegalArgumentException() {
		when(mockStudentProfileRepository.existsById(anyLong())).thenReturn(true);
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> profileServiceImpl.createStudentProfile(1L, ProfileRequestDTO.builder().build()));
		verify(mockStudentProfileRepository).existsById(anyLong());
		assertTrue(ex.getMessage().equals("Student profile with id " + 1L + " already exists."));
	}

	@Test
	void test_createStudentProfile_NonExistingProfile_Save() {
		when(mockStudentProfileRepository.existsById(anyLong())).thenReturn(false);
		long userId = 1L;
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1985, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("9876543210")
				.addressLine1("123 Main St")
				.addressLine2("Apt 4B")
				.city("New York")
				.state("NY")
				.pinCode("123456")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2001)
				.percentage(new BigDecimal(65.20))
				.build();
		StudentProfile expectedSavedStudentProfile = StudentProfile.builder()
				.userLogin(UserLogin.builder().userLoginId(userId).build())
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
				.yearOfPassing((short)dto.getYearOfPassing())
				.percentage(dto.getPercentage())
				.build();
		when(mockStudentProfileRepository.save(any(StudentProfile.class))).thenReturn(expectedSavedStudentProfile);
		profileServiceImpl.createStudentProfile(userId, dto);
		verify(mockStudentProfileRepository).existsById(anyLong());

		ArgumentCaptor<StudentProfile> argCaptorStudentProfile =
				ArgumentCaptor.forClass(StudentProfile.class);
		verify(mockStudentProfileRepository).save(argCaptorStudentProfile.capture());
		StudentProfile toSaveStudentProfile = argCaptorStudentProfile.getValue();
		assertEquals(userId, toSaveStudentProfile.getUserLogin().getUserLoginId());
		assertEquals("John", toSaveStudentProfile.getFirstName());
		assertEquals("Doe", toSaveStudentProfile.getLastName());
		assertEquals("9876543210", toSaveStudentProfile.getPhoneNumber());
	}
}
