package com.AirBnb.Final.Project.Dto;
import com.AirBnb.Final.Project.Enum.Gender;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// we use guest dto when we add new guest in booking
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto {

    @Positive(message = "guest id must be positive")
    private Long id;

    @Size(min = 5,max = 40,message = "guest name characters must be in range 5 to 40")
    private String name;

    @Positive(message = "age should be at least id must be 1")
    private int age;

    private Gender gender;

    private String user;


}
