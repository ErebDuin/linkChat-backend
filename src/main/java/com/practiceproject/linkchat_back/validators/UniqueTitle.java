package com.practiceproject.linkchat_back.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UniqueTitleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTitle {
    String message() default "Duplicate Name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
