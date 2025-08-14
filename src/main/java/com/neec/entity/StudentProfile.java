package com.neec.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.neec.enums.EnumGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE) @Getter @Setter
@Entity
@Table(name = "STUDENT_PROFILE")
public class StudentProfile {
	@Column(name = "PROFILE_ID", insertable = true, updatable = false, nullable = false, unique = true)
	@Id
	Long studentProfileId;

	@OneToOne
	@MapsId	// Tells JPA to use the ID from the UserLogin entity
	@JoinColumn(name = "PROFILE_ID")
	UserLogin userLogin;

	@Column(name = "FIRST_NAME", insertable = true, updatable = true, nullable = false, unique = false)
	String firstName;

	@Column(name = "LAST_NAME", insertable = true, updatable = true, nullable = false, unique = false)
	String lastName;

	@Column(name = "DATE_OF_BIRTH", insertable = true, updatable = true, nullable = false, unique = false)
	LocalDate dateOfBirth;

	@Column(name = "GENDER", insertable = true, updatable = true, nullable = false, unique = false, length = 1)
	@Enumerated(EnumType.STRING)
	EnumGender gender;

	@Column(name = "PHONE_NUMBER", insertable = true, updatable = true, nullable = false, unique = true)
	String phoneNumber;

	@Column(name = "ADDRESS_LINE1", insertable = true, updatable = true, nullable = false, unique = false)
	String addressLine1;

	@Column(name = "ADDRESS_LINE2", insertable = true, updatable = true, nullable = true, unique = false)
	String addressLine2;

	@Column(name = "CITY", insertable = true, updatable = true, nullable = false, unique = false)
	String city;

	@Column(name = "STATE", insertable = true, updatable = true, nullable = false, unique = false)
	String state;

	@Column(name = "PIN_CODE", insertable = true, updatable = true, nullable = false, unique = false)
	String pinCode;

	@Column(name = "SCHOOL_NAME", insertable = true, updatable = true, nullable = false, unique = false)
	String schoolName;

	@Column(name = "BOARD_NAME", insertable = true, updatable = true, nullable = false, unique = false)
	String boardName;

	@Column(name = "YEAR_OF_PASSING", insertable = true, updatable = true, nullable = false, unique = false)
	short yearOfPassing;

	@Column(name = "PERCENTAGE", insertable = true, updatable = false, nullable = false, unique = false)
	BigDecimal percentage;

	@Column(name = "ACTIVE", insertable = false, updatable = true, nullable = false, unique = false)
	boolean active;

	@Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false, unique = false)
	OffsetDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = true, updatable = true, nullable = false, unique = false)
	OffsetDateTime updatedAt;

	@PrePersist
	void onCreate() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
	}
}
