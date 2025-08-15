package com.neec.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.neec.validation.ExamYearValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExamYearValidator.class)
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ExamPassingYear {
	String message() default "Exam passing year must be in the past";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
