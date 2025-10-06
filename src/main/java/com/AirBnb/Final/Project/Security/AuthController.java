package com.AirBnb.Final.Project.Security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpLoginService signUpLoginService;

    @PostMapping(path = "/signup")
    public ResponseEntity<SignUpReturnDto> signup(@RequestBody @Valid SignUpDto signUpDto){

        SignUpReturnDto signUpReturnDto=signUpLoginService.signup(signUpDto);

        return new ResponseEntity<>(signUpReturnDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AccessRefreshDto> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){

        AccessRefreshDto accessRefreshDto=signUpLoginService.login(loginDto);

        Cookie cookie=new Cookie("refreshToken",accessRefreshDto.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(accessRefreshDto,HttpStatus.CREATED);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AccessRefreshDto> refresh(HttpServletRequest request, HttpServletResponse response){

       Cookie[] cookies=request.getCookies();

       AccessRefreshDto accessRefreshDto=null;

       for (Cookie cookie:cookies){

           if (cookie.getName().equals("refreshToken")){

               String refreshToken=cookie.getValue();
               accessRefreshDto=signUpLoginService.refresh(refreshToken);
               break;
           }

       }

       return new ResponseEntity<>(accessRefreshDto,HttpStatus.CREATED);
    }

    @PostMapping(path = "/adminAccess")
    ResponseEntity<AdminReturnDto> adminAccess(@RequestBody @Valid SignUpDto signUpDto){

        AdminReturnDto admin=signUpLoginService.adminAccess(signUpDto);

        return new ResponseEntity<>(admin,HttpStatus.CREATED);
    }

}
