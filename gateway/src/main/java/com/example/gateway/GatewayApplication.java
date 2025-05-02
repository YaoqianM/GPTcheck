package com.example.gateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

//zuul vs spring cloud gateway
//ribbon
@EnableEurekaClient
@EnableZuulProxy

@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class
        }
)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    //http://search-service/weather/search
    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
    @Qualifier("webFluxConversionService")
    public ConversionService webFluxConversionService() {
        return new DefaultFormattingConversionService();
    }
}
