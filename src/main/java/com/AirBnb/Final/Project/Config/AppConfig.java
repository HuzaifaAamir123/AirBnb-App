package com.AirBnb.Final.Project.Config;

import com.AirBnb.Final.Project.Auth.AuditorAwareImpl;
import com.stripe.Stripe;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAwareImpl")
public class AppConfig {


    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    AuditorAware<String> getAuditorAwareImpl(){
        return new AuditorAwareImpl();
    }




}
