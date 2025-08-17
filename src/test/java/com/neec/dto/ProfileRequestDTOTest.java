package com.neec.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.neec.enums.EnumGender;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ProfileRequestDTOTest {
	private Validator validator;

	@BeforeEach
	void setup() {
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	//  firstName Test Cases
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  "})
	void test_firstName_invalid(String input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.firstName(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "firstName");
		assertFalse(violations.isEmpty(), "Expected validation to fail for firstName: [" + input + "]");
	}

	@ParameterizedTest
	@ValueSource(strings = {"John"})
	void test_firstName_valid(String input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.firstName(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "firstName");
		assertTrue(violations.isEmpty(), "Unexpected validation to fail for firstName: [" + input + "]");
	}

	// lastName Test Cases - skipped as similar to firstName

	// dateOfBirth Test Cases
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"2025-08-14", "2027-08-01"})
	void test_dateOfBirth_invalid(String input) {
		LocalDate date = input == null ? null : LocalDate.parse(input);
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.dateOfBirth(date)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "dateOfBirth");
		assertFalse(violations.isEmpty(), "Expected validation to fail for dateOfBirth: [" + input + "]");
	}

	@Test
	void test_dateOfBirth_valid() {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.dateOfBirth(LocalDate.now().minusYears(20))
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "dateOfBirth");
		assertTrue(violations.isEmpty());
	}

	// gender Test Cases
	@ParameterizedTest
	@NullSource
	void test_gender_null(EnumGender input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.gender(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "gender");
		assertFalse(violations.isEmpty());
	}

	@ParameterizedTest
	@EnumSource(value = EnumGender.class)
	void test_gender_valid(EnumGender input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.gender(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "gender");
		assertTrue(violations.isEmpty());
	}

	// phoneNumber Test Cases
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "123456789", "1234567890", "01234567890", "98765432.10"})
	void test_phoneNumber_invalid(String input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.phoneNumber(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "phoneNumber");
		assertFalse(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(strings = {"9876543210", "8765432190", "7654321890"})
	void test_phoneNumber_valid(String input) {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.phoneNumber(input)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "phoneNumber");
		assertTrue(violations.isEmpty());
	}

	// addressLine1 Test Cases - skipped as similar to firstName
	// addressLine1 - can be blank
	// city Test Cases - skipped as similar to firstName
	// state Test Cases - skipped as similar to firstName
	// pinCode Test Cases - skipped as similar to phoneNumber
	// schoolName Test Cases - skipped as similar to firstName
	// boardName Test Cases - skipped as similar to firstName

	// yearOfPassing Test Cases
	@Test
	void test_yearOfPassing_invalid() {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.yearOfPassing((short)-1985)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "yearOfPassing");
		assertFalse(violations.isEmpty());
	}

	@Test
	void test_yearOfPassing_valid() {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.yearOfPassing((short)1985)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "yearOfPassing");
		assertTrue(violations.isEmpty());
	}

	// percentage Test Cases
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-1.0", "101.00"})
	void test_percentage_invalid(String input) {
		BigDecimal percentage = input == null ? null : new BigDecimal(input);
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.percentage(percentage)
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "percentage");
		assertFalse(violations.isEmpty());
	}

	@Test
	void test_percentage_valid() {
		ProfileRequestDTO dto = ProfileRequestDTO.builder()
				.percentage(new BigDecimal(98.20))
				.build();
		Set<ConstraintViolation<ProfileRequestDTO>> violations = validator.validateProperty(dto, "percentage");
		assertTrue(violations.isEmpty());
	}
}
