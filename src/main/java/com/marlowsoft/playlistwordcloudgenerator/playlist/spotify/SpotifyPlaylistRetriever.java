package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Token;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SpotifyPlaylistRetriever implements PlaylistRetriever<Playlist, String> {
  private static final Logger LOGGER = LogManager.getLogger(SpotifyPlaylistRetriever.class);

  private static final String CLIENT_ID;

  private static final String CLIENT_SECRET;

  static {
    final Map<String, String> environmentVars = System.getenv();

    CLIENT_ID = environmentVars.getOrDefault("SPOTIFY_CLIENT_ID", "");
    CLIENT_SECRET = environmentVars.getOrDefault("SPOTIFY_CLIENT_SECRET", "");

    if (CLIENT_ID.isEmpty()) {
      LOGGER.warn(
          "SPOTIFY_CLIENT_ID was not set. This will probably result in 401s when calling the Spotify API. Set this in the environment variables.");
    }
    if (CLIENT_SECRET.isEmpty()) {
      LOGGER.warn(
          "SPOTIFY_CLIENT_SECRET was not set. This will probably result in 401s when calling the Spotify API. Set this in the environment variables.");
    }
  }

  private final ObjectMapper objectMapper;

  @Autowired
  SpotifyPlaylistRetriever(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Playlist getPlaylist(String identifier) throws IOException, InterruptedException {
    // TODO inject this builder instead of hardcoding the "real" one
    try (final HttpClient client = HttpClient.newBuilder().build()) {
      final HttpResponse<String> response =
          client.send(
              HttpRequest.newBuilder()
                  .header("Authorization", String.format("Bearer %s", getOauthToken()))
                  .uri(
                      UriComponentsBuilder.newInstance()
                          .scheme("https")
                          .host("api.spotify.com")
                          .path("v1/playlists/{playlist-id}")
                          .buildAndExpand(identifier)
                          .toUri())
                  .GET()
                  .build(),
              HttpResponse.BodyHandlers.ofString());
      LOGGER.info("i got this back from Spotify: {}", response.body());

      return objectMapper.readValue(response.body(), Playlist.class);
    }
  }

  private String getOauthToken() throws IOException, InterruptedException {
    // TODO inject this builder instead of hardcoding the "real" one
    try (final HttpClient client = HttpClient.newBuilder().build()) {
      final UriComponents uriComponents =
          UriComponentsBuilder.newInstance()
              .scheme("https")
              .host("accounts.spotify.com")
              .path("api/token")
              .build();
      // TODO does Spring have a way to get oauth tokens?
      final HttpRequest request =
          HttpRequest.newBuilder()
              .header("Content-Type", "application/x-www-form-urlencoded")
              .uri(uriComponents.toUri())
              .POST(
                  HttpRequest.BodyPublishers.ofString(
                      String.format(
                          "grant_type=client_credentials&client_id=%s&client_secret=%s",
                          CLIENT_ID, CLIENT_SECRET)))
              .build();
      final HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());
      final Token token = objectMapper.readValue(response.body(), Token.class);
      return token.getAccessToken();
    }
  }
}
