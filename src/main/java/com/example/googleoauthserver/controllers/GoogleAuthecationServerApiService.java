package com.example.googleoauthserver.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import com.google.api.client.auth.oauth2.Credential;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthecationServerApiService {
    private final ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    public GoogleAuthecationServerApiService(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public Map<String,String> refreshToken(String refreshToken) throws IOException {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        String clientId = clientRegistration.getClientId();
        String secret = clientRegistration.getClientSecret();

                Credential credential = new GoogleCredential.Builder()
                        .setTransport(new NetHttpTransport())
                        .setJsonFactory(new JacksonFactory())
                        .setClientSecrets(clientId, secret)
                        .build();

        GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        tokenResponse.setRefreshToken(refreshToken);

        credential.setFromTokenResponse(tokenResponse);
        credential.refreshToken();
        Map<String,String> map = new HashMap<>();
        map.put("access_token", credential.getAccessToken());
        map.put("access_token_ExpressIn", credential.getAccessToken().toString());
        if(credential.refreshToken()){
            map.put("refresh_token", credential.getRefreshToken());
            map.put("refresh_token_ExpressIn", credential.getExpiresInSeconds().toString());
        }
        return map;
    }
}
