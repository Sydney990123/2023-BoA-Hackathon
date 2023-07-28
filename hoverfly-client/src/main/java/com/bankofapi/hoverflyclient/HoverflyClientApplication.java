package com.bankofapi.hoverflyclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@SpringBootApplication
public class HoverflyClientApplication {
    private static final int HOVERFLY_PORT = 8500;
    private static final String HOVERFLY_HOST = "localhost";

    public static void main(String[] args) {
        SpringApplication.run(HoverflyClientApplication.class, args);
    }

    @Bean
    @Profile("TEST")
    public RestTemplate getHoverflyProxiedRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HOVERFLY_HOST, HOVERFLY_PORT));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    @Bean
    @Profile({"PROD","default"})
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
