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
        validatedBy = {RoomBasePriceValidator.class}
)
public @interface RoomBasePriceAnnotation {

    String message() default "{room base price must be 500 or above 500}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
