package com.AirBnb.Final.Project.Annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MailValidator implements ConstraintValidator<MailAnnotation,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        boolean isDigit=false;

        if (s==null){
            return false;
        }

       for (int i=0;i<s.length();i++){

           char ch=s.charAt(i);

           if (Character.isDigit(ch)){
               isDigit=true;
               break;
           }

       }

       if (!isDigit){
           return false;
       }

        return s.endsWith("@gmail.com");
    }
}
