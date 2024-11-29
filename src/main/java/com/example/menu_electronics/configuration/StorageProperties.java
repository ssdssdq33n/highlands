package com.example.menu_electronics.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

// @Configuration
@ConfigurationProperties("storage")
@Data
public class StorageProperties {
    private String location;
}
