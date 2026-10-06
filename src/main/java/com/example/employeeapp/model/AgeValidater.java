package com.example.employeeapp.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidater implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            return false; // Or true if @NotNull is handled separately
        }
        return Period.between(dob, LocalDate.now()).getYears() >= 18;
    }
}
