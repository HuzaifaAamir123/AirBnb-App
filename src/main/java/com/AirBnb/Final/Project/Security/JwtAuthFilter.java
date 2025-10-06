package com.AirBnb.Final.Project.Security;


import com.AirBnb.Final.Project.Entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtRequestHeader=request.getHeader("Authorization");

        try {

            if (jwtRequestHeader==null || !jwtRequestHeader.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }

            String jwtToken=jwtRequestHeader.substring(7);

            Long userId=jwtService.verifyJwtToken(jwtToken);

            if (userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                User loginUser=userRepository.findById(userId)
                        .orElseThrow(()->new BadCredentialsException("user is not verified in jwt auth filter"));

                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        loginUser,null,loginUser.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request,response);

        }catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}
