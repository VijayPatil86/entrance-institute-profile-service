package com.neec.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.neec.enums.EnumGender;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE) @Getter @Setter
public class ProfileRequestDTO {
	@NotBlank(message = "first name can not be blank")
	String firstName;

	@NotBlank(message = "last name can not be blank")
	String lastName;

	@NotNull(message = "date of birth is required")
	@Past(message = "date of birth must be in past")
	LocalDate dateOfBirth;

	@NotNull(message = "geneder is required")
	EnumGender gender;

	@NotBlank(message = "phone number is required")
	@Size(min = 10, max = 10, message = "phone number must have exactly 10 digits")
	@Digits(fraction = 0, integer = 10, message = "phone number must have exactly 10 digits")
	@Pattern(regexp = "^[7-9][0-9]{9}$", message = "Phone number must be 10 digits starting with 7, 8, or 9")
	String phoneNumber;

	@NotBlank(message = "address is required")
	String addressLine1;

	String addressLine2;

	@NotBlank(message = "city is required")
	String city;

	@NotBlank(message = "state is required")
	String state;

	@NotBlank(message = "pin code is required")
	@Size(min = 6, max = 6, message = "pin code must have exactly 6 digits")
	@Digits(fraction = 0, integer = 6, message = "pin code must have exactly 6 digits")
	@Pattern(regexp = "^[0-9]{6}$")
	String pinCode;

	@NotBlank(message = "school name is required")
	String schoolName;

	@NotBlank(message = "board name is required")
	String boardName;

	@Positive(message = "year of passing can not be negative")
	short yearOfPassing;

	@NotNull(message = "percetage value can not be null")
	@DecimalMin(value = "0.0")
	@DecimalMax(value = "100.0")
	BigDecimal percentage;
}
