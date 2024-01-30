package com.example.googleoauthserver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class YoutubeChannelDataService {
    public String getYoutubeChannelId(String access_token) throws IOException {

        GoogleCredential credential = new GoogleCredential().setAccessToken(access_token);

        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();

        YouTube.Channels.List channelRequest
                = youtube.channels().list("id");

        channelRequest.setMine(true);
        channelRequest.setFields("items(id)");

        ChannelListResponse channelResponse = channelRequest.execute();

        String channelId = channelResponse.getItems().get(0).getId();

        return channelId;
    }
}
