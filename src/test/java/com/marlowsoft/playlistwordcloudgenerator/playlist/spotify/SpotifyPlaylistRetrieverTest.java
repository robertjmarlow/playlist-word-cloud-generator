package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpotifyPlaylistRetrieverTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  private PlaylistRetriever<Playlist, String> playlistRetriever;

  @Mock private HttpClient.Builder httpClientBuilder;

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<String> httpResponse;

  @BeforeEach
  void beforeEach() {
    playlistRetriever = new SpotifyPlaylistRetriever(OBJECT_MAPPER, httpClientBuilder);
  }

  @Test
  void getPlaylistSuccess() {}

  @Test
  void failGettingOauthToken() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(400);
    when(httpResponse.body())
        .thenReturn(
            Resources.toString(
                Resources.getResource("spotify/invalid-client.json"), StandardCharsets.UTF_8));

    final IOException exception =
        assertThrows(IOException.class, () -> playlistRetriever.getPlaylist("asdf"));

    assertTrue(exception.getMessage().contains("unsupported_grant_type"));
  }

  @Test
  void failGettingPlaylist() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(200, 401);
    when(httpResponse.body())
        .thenReturn(
            Resources.toString(Resources.getResource("spotify/token.json"), StandardCharsets.UTF_8),
            Resources.toString(
                Resources.getResource("spotify/no-authentication.json"), StandardCharsets.UTF_8));

    final IOException exception =
        assertThrows(IOException.class, () -> playlistRetriever.getPlaylist("asdf"));

    assertTrue(exception.getMessage().contains("401"));
    assertTrue(exception.getMessage().contains("No token provided"));
  }
}
