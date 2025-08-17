package com.neec.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.neec.enums.EnumGender;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE) @Getter
public class ProfileResponseDTO {
	String firstName;
	String lastName;
	LocalDate dateOfBirth;
	EnumGender gender;
	String phoneNumber;
	String addressLine1;
	String addressLine2;
	String city;
	String state;
	String pinCode;
	String schoolName;
	String boardName;
	short yearOfPassing;
	BigDecimal percentage;
	private Long studentProfileId;
	private String email;
}
