package com.bankofapi.hoverflyclient.web;

import com.bankofapi.hoverflyclient.model.GetAccounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class AccountsClientController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Environment env;

    @Value("${account.service.url:http://localhost:8080}")
    private String accountServiceUrl;


    @GetMapping("/accounts-client")
    public GetAccounts getAccounts() {

        System.out.println("Inside TestController::invoke()-->"+ Arrays.toString(env.getActiveProfiles()));
        String url = accountServiceUrl + "/accounts";
        GetAccounts response = restTemplate.exchange(url, HttpMethod.GET, null,
                GetAccounts.class).getBody();

        System.out.println("Actual Response : " + response);
        return response;
    }

}
