package com.example.googleoauthserver.controllers;

import com.example.googleoauthserver.service.YoutubeChannelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@RestController
public class OAuthController {
    private final YoutubeChannelDataService youtubeChannelDataService;
    @Autowired
    public OAuthController(YoutubeChannelDataService youtubeChannelDataService) {
        this.youtubeChannelDataService = youtubeChannelDataService;
    }

    @GetMapping("/")
    public String getUserInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        Map<String,String> map = new HashMap<>();
        if(authorizedClient.getAccessToken()!= null){
            try {
                String youtubeChannelId = youtubeChannelDataService
                        .getYoutubeChannelId(authorizedClient.getAccessToken().getTokenValue());
                map.put("youtubeChannelId", youtubeChannelId);
            } catch (IOException ex){
                map.put("youtubeChannelIdException", ex.getMessage());
            }

            map.put("access_token", authorizedClient.getAccessToken().getTokenValue());
            map.put("expires_at", authorizedClient.getAccessToken().getExpiresAt().toString());
            map.put("principal_name", authorizedClient.getPrincipalName());

        }
        if(authorizedClient.getRefreshToken()!=null) {
            var refreshToken = authorizedClient.getRefreshToken();
            map.put("refresh_token", refreshToken.getTokenValue());
            if(refreshToken.getIssuedAt()!=null){
                map.put("refresh_IssuedAt", refreshToken.getIssuedAt().toString());
            }
            if(refreshToken.getExpiresAt()!=null){
                map.put("refresh_ExpiresAt", refreshToken.getExpiresAt().toString());
            }
        }

        return map.toString();
    }
}
