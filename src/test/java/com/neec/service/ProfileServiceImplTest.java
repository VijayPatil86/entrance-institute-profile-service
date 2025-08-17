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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neec.dto.ProfileRequestDTO;
import com.neec.dto.ProfileResponseDTO;
import com.neec.entity.StudentProfile;
import com.neec.entity.UserLogin;
import com.neec.enums.EnumGender;
import com.neec.repository.StudentProfileRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
	@Mock
	private StudentProfileRepository mockStudentProfileRepository;
	@Mock
	private EntityManager mockEntityManager;
	@InjectMocks
	private ProfileServiceImpl profileServiceImpl;

	@Test
	void test_createStudentProfile_ExistingProfile_Raise_IllegalArgumentException() {
		when(mockStudentProfileRepository.existsById(anyLong())).thenReturn(true);
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> profileServiceImpl.saveProfile(1L, ProfileRequestDTO.builder().build()));
		verify(mockStudentProfileRepository).existsById(anyLong());
		assertTrue(ex.getMessage().equals("Student profile with id " + 1L + " already exists."));
	}

	@Test
	void test_createStudentProfile_NonExistingProfile_Save() {
		when(mockStudentProfileRepository.existsById(anyLong())).thenReturn(false);
		UserLogin userLogin = UserLogin.builder().userLoginId(1L).build();
		when(mockEntityManager.getReference(UserLogin.class, 1L)).thenReturn(userLogin);
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
				.yearOfPassing((short)dto.getYearOfPassing())
				.percentage(dto.getPercentage())
				.build();
		when(mockStudentProfileRepository.save(any(StudentProfile.class))).thenReturn(expectedSavedStudentProfile);
		profileServiceImpl.saveProfile(userId, dto);
		verify(mockStudentProfileRepository).existsById(anyLong());
		verify(mockEntityManager).getReference(UserLogin.class, 1L);

		ArgumentCaptor<StudentProfile> argCaptorStudentProfile =
				ArgumentCaptor.forClass(StudentProfile.class);
		verify(mockStudentProfileRepository).save(argCaptorStudentProfile.capture());
		StudentProfile toSaveStudentProfile = argCaptorStudentProfile.getValue();
		assertEquals(userId, toSaveStudentProfile.getUserLogin().getUserLoginId());
		assertEquals("John", toSaveStudentProfile.getFirstName());
		assertEquals("Doe", toSaveStudentProfile.getLastName());
		assertEquals("9876543210", toSaveStudentProfile.getPhoneNumber());
	}

	@Test
	void test_getProfileForUserId_NonExisting_UserId() {
		Long userId = 999L;
		when(mockStudentProfileRepository.findById(anyLong()))
			.thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> profileServiceImpl.getProfileByUserId(userId));
		verify(mockStudentProfileRepository).findById(anyLong());
		assertTrue(ex.getMessage().equals("Student profile with id " + userId + " does not exist."));
	}

	@Test
	void test_getProfileForUserId_Existing_UserId() {
		Long userId = 1L;
		UserLogin userLogin = UserLogin.builder()
				.userLoginId(userId)
				.emailAddress("test@gmail.com")
				.build();
		StudentProfile profile = StudentProfile.builder()
				.studentProfileId(userId)
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1980, 01, 01))
				.city("New York")
				.state("New York")
				.pinCode("415236")
				.addressLine1("ABC Road")
				.boardName("ABC Board")
				.gender(EnumGender.M)
				.percentage(BigDecimal.valueOf(65.30))
				.phoneNumber("9876543210")
				.schoolName("ABC School")
				.userLogin(userLogin)
				.build();
		when(mockStudentProfileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
		ProfileResponseDTO responseDTO = profileServiceImpl.getProfileByUserId(userId);
		verify(mockStudentProfileRepository).findById(anyLong());
		assertEquals(userId, responseDTO.getStudentProfileId());
		assertEquals(profile.getFirstName(), responseDTO.getFirstName());
		assertEquals(profile.getLastName(), responseDTO.getLastName());
		assertEquals(profile.getDateOfBirth(), responseDTO.getDateOfBirth());
		assertEquals(profile.getCity(), responseDTO.getCity());
		assertEquals(profile.getState(), responseDTO.getState());
		assertEquals(profile.getPinCode(), responseDTO.getPinCode());
		assertEquals(profile.getAddressLine1(), responseDTO.getAddressLine1());
		assertEquals(profile.getBoardName(), responseDTO.getBoardName());
		assertEquals(profile.getGender(), responseDTO.getGender());
		assertEquals(profile.getPercentage(), responseDTO.getPercentage());
		assertEquals(profile.getPhoneNumber(), responseDTO.getPhoneNumber());
		assertEquals(profile.getSchoolName(), responseDTO.getSchoolName());
		assertEquals(profile.getUserLogin().getEmailAddress(), responseDTO.getEmail());
	}

	@Test
	void test_updateProfileForUserId_NonExisting_UserId() {
		Long nonExistingUserId = 999L;
		when(mockStudentProfileRepository.findById(anyLong()))
			.thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> profileServiceImpl.updateProfile(nonExistingUserId,
						ProfileRequestDTO.builder().build()));
		verify(mockStudentProfileRepository).findById(anyLong());
		assertTrue(ex.getMessage().equals("Student profile with id " + nonExistingUserId + " does not exist."));
	}

	@Test
	void test_updateProfileForUserId_Existing_UserId() {
		Long existingUserId = 1L;
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1985, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("9876543210")
				.addressLine1("456 Main St")
				.addressLine2("Apt 2A")
				.city("New York")
				.state("NY")
				.pinCode("452361")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2001)
				.percentage(new BigDecimal(65.20))
				.build();
		StudentProfile foundStudentProfile = StudentProfile.builder()
				.firstName("John")
				.lastName("Doe")
				.dateOfBirth(LocalDate.of(1986, 10, 11))
				.gender(EnumGender.M)
				.phoneNumber("7894561230")
				.addressLine1("123 Main St")
				.addressLine2("Apt 4B")
				.city("New York")
				.state("NY")
				.pinCode("542136")
				.schoolName("XYZ High School")
				.boardName("CBSE")
				.yearOfPassing((short)2001)
				.percentage(new BigDecimal(65.20))
				.build();
		StudentProfile savedStudentProfile = StudentProfile.builder().build();
		when(mockStudentProfileRepository.findById(anyLong())).thenReturn(Optional.of(foundStudentProfile));
		when(mockStudentProfileRepository.save(any(StudentProfile.class))).thenReturn(savedStudentProfile);
		profileServiceImpl.updateProfile(existingUserId, dto);
		verify(mockStudentProfileRepository).findById(anyLong());

		ArgumentCaptor<StudentProfile> argCaptorStudentProfile =
				ArgumentCaptor.forClass(StudentProfile.class);
		verify(mockStudentProfileRepository).save(argCaptorStudentProfile.capture());
		StudentProfile toSaveStudentProfile = argCaptorStudentProfile.getValue();
		assertEquals("John", toSaveStudentProfile.getFirstName());
		assertEquals("Doe", toSaveStudentProfile.getLastName());
		assertEquals(LocalDate.of(1985, 10, 11), toSaveStudentProfile.getDateOfBirth());
		assertEquals(EnumGender.M, toSaveStudentProfile.getGender());
		assertEquals("9876543210", toSaveStudentProfile.getPhoneNumber());
		assertEquals("456 Main St", toSaveStudentProfile.getAddressLine1());
		assertEquals("Apt 2A", toSaveStudentProfile.getAddressLine2());
		assertEquals("New York", toSaveStudentProfile.getCity());
		assertEquals("NY", toSaveStudentProfile.getState());
		assertEquals("452361", toSaveStudentProfile.getPinCode());
		assertEquals("XYZ High School", toSaveStudentProfile.getSchoolName());
		assertEquals("CBSE", toSaveStudentProfile.getBoardName());
		assertEquals(2001, toSaveStudentProfile.getYearOfPassing());
	}
}
