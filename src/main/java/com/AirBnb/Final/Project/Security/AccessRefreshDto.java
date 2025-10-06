package com.AirBnb.Final.Project.Security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessRefreshDto {

    private Long id;

    private String accessToken;

    private String refreshToken;

    private String message;
}
