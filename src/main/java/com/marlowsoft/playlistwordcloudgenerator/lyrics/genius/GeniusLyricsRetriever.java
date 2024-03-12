package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeniusLyricsRetriever implements LyricsRetriever<LyricsResponse, LyricsRequest> {
  private static final Logger LOGGER = LogManager.getLogger(GeniusLyricsRetriever.class);

  private static final String CLIENT_ACCESS_TOKEN;

  static {
    final Map<String, String> environmentVars = System.getenv();

    CLIENT_ACCESS_TOKEN = environmentVars.getOrDefault("GENIUS_CLIENT_ACCESS_TOKEN", "");

    if (CLIENT_ACCESS_TOKEN.isEmpty()) {
      LOGGER.warn(
          "GENIUS_CLIENT_CLIENT_ACCESS_TOKEN was not set. This will probably result in 401s when calling the Genius API. Set this in the environment variables.");
    }
  }

  private final HttpClient.Builder httpClientBuilder;

  @Autowired
  GeniusLyricsRetriever(HttpClient.Builder httpClientBuilder) {
    this.httpClientBuilder = httpClientBuilder;
  }

  @Override
  public LyricsResponse getLyrics(LyricsRequest request) throws IOException, InterruptedException {
    try (final HttpClient client = httpClientBuilder.build()) {
      final HttpResponse<String> songResponse =
          client.send(
              HttpRequest.newBuilder()
                  .header("Authorization", String.format("Bearer %s", CLIENT_ACCESS_TOKEN))
                  .uri(
                      UriComponentsBuilder.newInstance()
                          .scheme("https")
                          .host("api.genius.com")
                          .pathSegment("songs", "{songId}")
                          .build("378195"))
                  .GET()
                  .build(),
              HttpResponse.BodyHandlers.ofString());
      if (songResponse.statusCode() == 200) {
        return ImmutableLyricsResponse.builder()
            .addLyrics("syrup sandwiches", "syrup, syrup, syrup sandwiches")
            .build();
      }
      LOGGER.error("Something went wrong when authorizing with Genius: {}", songResponse.body());
      throw new IOException(
          String.format("Error authorizing with Genius: %s", songResponse.body()));
    }
  }
}
