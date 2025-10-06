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
        validatedBy = {MailValidator.class}
)
public @interface MailAnnotation {

    String message() default "{email must be end with @gmail.com pattern and contains at least one digit}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
