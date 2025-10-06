package com.AirBnb.Final.Project.Security;


import com.AirBnb.Final.Project.Annotation.MailAnnotation;
import com.AirBnb.Final.Project.Annotation.PasswordAnnotation;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5,max = 40,message = "user name characters must be in range 5 to 40")
    private String name;

    @MailAnnotation
    private String email;

    @PasswordAnnotation
    private String password;

}
