package com.AirBnb.Final.Project.Security;


import com.AirBnb.Final.Project.Annotation.MailAnnotation;
import com.AirBnb.Final.Project.Annotation.PasswordAnnotation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @MailAnnotation
    private String email;

    @PasswordAnnotation
    private String password;

}
