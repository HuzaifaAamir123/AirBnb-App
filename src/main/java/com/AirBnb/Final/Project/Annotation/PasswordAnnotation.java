package com.AirBnb.Final.Project.Annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {PasswordValidator.class}
)
public @interface PasswordAnnotation {

    String message() default "{password must contain at least one uppercase,one lowercase and one digit}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
