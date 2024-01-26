package com.example.googleoauthserver.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
public class OAuthController {
    @GetMapping("/")
    public String getUserInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        Map<String,String> map = new HashMap<>();
        if(authorizedClient.getAccessToken()!= null){
            map.put("access_token", authorizedClient.getAccessToken().getTokenValue());
            map.put("expires_at", authorizedClient.getAccessToken().getExpiresAt().toString());
            map.put("principal_name", authorizedClient.getPrincipalName());
        }
        if(authorizedClient.getRefreshToken()!=null){
            map.put("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
            map.put("refresh_token_expires_at", authorizedClient.getRefreshToken().getExpiresAt().toString());
        }

        return map.toString();
    }
}
