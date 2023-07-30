package com.bankofapi.account.web;

import com.bankofapi.account.model.GetAccountBalancesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bankofapi.account.model.GetAccountsDTO;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@RestController
public class AccountsController {

    RestTemplate restTemplate = new RestTemplate();

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    Environment env;

    String ACCOUNT_URL = "https://ob.sandbox.natwest.com/open-banking/v3.1/aisp/accounts";

    String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHAiOiJCb0FUZXN0QXBwIiwib3JnIjoiYm9hdGVzdHRlYW0uY28udWsiLCJpc3MiOiJodHRwczovL2FwaS5zYW5kYm94Lm5hdHdlc3QuY29tIiwidG9rZW5fdHlwZSI6IkFDQ0VTU19UT0tFTiIsImV4dGVybmFsX2NsaWVudF9pZCI6IkJEd2V1TXhPZnFYY2FIbk9TWXoyZGRBX1pjeEpXU21OUExTUHJ3cHpPMjA9IiwiY2xpZW50X2lkIjoiNWNlNTFjMTMtYWZiMy00MjBkLTk0MTAtYTYzNjQzN2RiNGQ5IiwibWF4X2FnZSI6ODY0MDAsImF1ZCI6IjVjZTUxYzEzLWFmYjMtNDIwZC05NDEwLWE2MzY0MzdkYjRkOSIsInVzZXJfaWQiOiIxMjM0NTY3ODkwMTJAYm9hdGVzdHRlYW0uY28udWsiLCJncmFudF9pZCI6ImIyYzRhOTgxLTBmZDAtNGEwZi1hZDczLTExOGU1NDBkNGI2YSIsInNjb3BlIjoiYWNjb3VudHMgb3BlbmlkIiwiY29uc2VudF9yZWZlcmVuY2UiOiI4YTU2NDNmMC1lZTQwLTRlYjItYWJkZS04NWRjYjJjMzQxMDIiLCJleHAiOjE2OTA1NDQxMjgsImlhdCI6MTY5MDU0MzUyOCwianRpIjoiNjE5YjU0N2QtOTcwMy00MzEzLTkyNDktYzhkYWM2N2NmNzAyIiwidGVuYW50IjoiTmF0V2VzdCJ9.fo1n9lXavszZoYnKi4sdBOnczea_4ZqDgdDbVjsSQXIvTBUU4VYUbjdL5-784EheXinJxnBXfqjmgIStw8cE5lYdlq1Jsss8-kTZw-pGMl_hoiNJRLWez47Ek1otrx7rh3E4nfx8BiesuCyvQKRtJMfFPEA3GJrl4_pclnA2Ya7FxUqdXrXFJDUoUMsPk7YzV4tAC4ULUvWb37alWzUtxUiVDSXAPI1a05r7IHNqa-7HQtL9rsPwR1JSgXdFfVNqyzDf_pATEOoTWVtCbKVu-7lJo39YB5iFNZMtm1H2cHiO7WTZKUStvXWTf40uvlF6IT3tYQSC6qdFrngJXdxKnQ";

    private static final TrustManager MOCK_TRUST_MANAGER = new X509ExtendedTrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

        }

    };

    private RestTemplate createRestTemplate() throws Exception {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("userproxy.rbsgrp.net", 8080),
                new NTCredentials(
                        System.getProperty("http.proxyUser"),
                        System.getProperty("http.proxyPassword"),
                        "userproxy.rbsgrp.net",
                        "EUROPA"));
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{MOCK_TRUST_MANAGER}, new SecureRandom());
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultCredentialsProvider(credsProvider).disableCookieManagement();
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier()));
        httpClientBuilder.setProxy(new HttpHost("userproxy.rbsgrp.net", 8080));
        HttpComponentsClientHttpRequestFactory requestFactory =
               new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());

        return new RestTemplate(requestFactory);
    }

    @GetMapping("/accounts")
    public GetAccountsDTO getAccounts() throws Exception {
        restTemplate = createRestTemplate();
        System.out.println("Inside TestController::invoke()-->"+ Arrays.toString(env.getActiveProfiles()));
        String url = ACCOUNT_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<String>() {
                }).getBody();

        System.out.println("Actual Response : " + response);
        return mapper.readValue(response, GetAccountsDTO.class);
    }

    @GetMapping("/{accountId}/balances")
    public GetAccountBalancesDTO getBalances(@PathVariable String accountId) throws Exception {
        restTemplate = createRestTemplate();
        System.out.println("Inside TestController::invoke()-->"+ Arrays.toString(env.getActiveProfiles()));
        String url = ACCOUNT_URL + "/" + accountId + "/balances";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<String>() {
                }).getBody();

        System.out.println("Actual Response : " + response);
        return mapper.readValue(response, GetAccountBalancesDTO.class);
    }

}
