package com.AirBnb.Final.Project.Security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReturnDto {

    private String name;

    private String email;

    private String message;
}
