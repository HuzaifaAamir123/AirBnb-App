package com.AirBnb.Final.Project.Annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordAnnotation,String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s==null){
            return false;
        }

        boolean uppercase=false;
        boolean lowercase=false;
        boolean specialcase=false;
        boolean isDigit=false;

        for (int i=0;i<s.length();i++){

            char ch=s.charAt(i);

            if (ch>='a' && ch<='z'){
                lowercase=true;
            }

            if (ch>='A' && ch<='Z'){
                uppercase=true;
            }

            if ((ch>=32 && ch<=47) || ch==64){
                specialcase=true;
            }

            if (Character.isDigit(ch)){
                isDigit=true;
            }

        }

        if (uppercase && lowercase && specialcase && isDigit){
            return true;
        }

        return false;
    }

}
