package com.example.employeeapp.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidater.class)
@Documented
public @interface Adult {
    String message() default "Age must be greater than or equal to 18";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

