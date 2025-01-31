/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author leecjong
 */
@Configuration
@PropertySource("classpath:conf/db.properties")
public class DbConfig {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigDb() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
