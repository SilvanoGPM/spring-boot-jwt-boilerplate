package com.skyg0d.spring.jwt.property;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.init")
@Getter
@Setter
@ToString
public class DefaultDataProps {

    @NestedConfigurationProperty
    private DefaultUser user = new DefaultUser();

    @Getter
    @Setter
    @ToString
    public static class DefaultUser {

        private String email = "admin@mail.com";
        private String password = "admin123";
        private String username = "Admin";

    }

}
