package com.neec.validation;

import java.time.Year;
import java.time.ZoneOffset;

import com.neec.validation.annotation.ExamPassingYear;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExamYearValidator implements ConstraintValidator<ExamPassingYear, Short> {
	@Override
	public boolean isValid(Short exammPassingYear, ConstraintValidatorContext context) {
		if(exammPassingYear == null)
			return true;	// Let @Null handle null if needed
		int currentYear = Year.now(ZoneOffset.UTC).getValue();
		return exammPassingYear < currentYear;
	}
}
