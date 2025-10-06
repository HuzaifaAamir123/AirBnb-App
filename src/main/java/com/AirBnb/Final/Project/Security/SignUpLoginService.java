package com.AirBnb.Final.Project.Security;


import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Enum.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpLoginService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;

    public SignUpReturnDto signup(SignUpDto signUpDto){

        log.info("user start to sign up in the application");

        Optional<User>optionalUser=userRepository.findByEmail(signUpDto.getEmail().toLowerCase());

        if (optionalUser.isPresent()){
            log.error("user with email:{} is already exist",signUpDto.getEmail().toLowerCase());
            throw new BadCredentialsException("user with email: "+signUpDto.getEmail().toLowerCase()+" is already exist");
        }

        User newUser=User.builder()
                .name(signUpDto.getName().toLowerCase())
                .email(signUpDto.getEmail().toLowerCase())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(Set.of(Role.GUEST))
                .build();

        newUser= userRepository.save(newUser);

        log.info("user successfully sign up in the application");

        return new SignUpReturnDto(newUser.getName(), newUser.getEmail(),"SignUp Successfully");
    }

    public AccessRefreshDto login(LoginDto loginDto){

        log.info("user start to login in the application");

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
          loginDto.getEmail(),loginDto.getPassword()
        );

        Authentication authentication=authenticationManager.authenticate(authenticationToken);

        User loginUser= (User) authentication.getPrincipal();

        String accessToken=jwtService.generateAccessToken(loginUser);
        String refreshToken=jwtService.generateRefreshToken(loginUser);

        sessionService.generateSession(loginUser,refreshToken);

        log.info("user successfully login in the application");

        return new AccessRefreshDto(loginUser.getId(),accessToken,refreshToken,"Login Successfully");
    }

    public AccessRefreshDto refresh(String refreshToken){

        log.info("refresh request start here");

        Long userId= jwtService.verifyJwtToken(refreshToken);

        sessionService.verifySession(refreshToken);

        User loginUser=userRepository.findById(userId)
                .orElseThrow(()->{
                    log.error("token refresh error occured");
                    return new BadCredentialsException("token refresh error occured");
                });

        String accessToken=jwtService.generateAccessToken(loginUser);

        log.info("refresh request done successfully here");

        return new AccessRefreshDto(loginUser.getId(),accessToken,refreshToken,"Login Successfully");
    }


    public AdminReturnDto adminAccess(SignUpDto signUpDto) {

        log.info("user try to upgrade as a admin");

        Optional<User>optionalUser=userRepository.findByEmail(signUpDto.getEmail().toLowerCase());

        if (optionalUser.isPresent()){
            log.error("admin with email:{} is already exist",signUpDto.getEmail().toLowerCase());
            throw new BadCredentialsException("admin with email: "+signUpDto.getEmail().toLowerCase()+" is already exist");
        }

        User newAdmin=User.builder()
                .name(signUpDto.getName().toLowerCase())
                .email(signUpDto.getEmail().toLowerCase())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(Set.of(Role.HOTEL_MANAGER))
                .build();

        newAdmin= userRepository.save(newAdmin);

        log.info("user successfully upgraded as a admin");

        return new AdminReturnDto(newAdmin.getName(), newAdmin.getEmail(),"Admin Account Created Successfully");

    }
}
