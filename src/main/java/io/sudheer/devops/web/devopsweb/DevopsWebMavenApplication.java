package io.sudheer.devops.web.devopsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableAutoConfiguration
public class DevopsWebMavenApplication {
    public static void main (String[] args) {
        SpringApplication app = new SpringApplication(DevopsWebMavenApplication.class);
        app.run(args);
    }
    
    @Bean
    WebMvcConfigurer configurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers (ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/pages/**").
                          addResourceLocations("classpath:/static/");
            }
        };
    }
}
