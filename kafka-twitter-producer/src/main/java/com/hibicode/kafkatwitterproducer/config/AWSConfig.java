package com.hibicode.kafkatwitterproducer.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Bean
    public AWSKMS kmsClient() {
        return AWSKMSClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("app")).build();
    }

}
