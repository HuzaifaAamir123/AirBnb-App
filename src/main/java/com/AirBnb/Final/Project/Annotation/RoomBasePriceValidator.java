package com.AirBnb.Final.Project.Annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class RoomBasePriceValidator implements ConstraintValidator<RoomBasePriceAnnotation, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {

        if (bigDecimal==null){
            return false;
        }

        return bigDecimal.compareTo(BigDecimal.valueOf(500))>=0;
    }

}
