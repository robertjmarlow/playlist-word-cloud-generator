package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.ImmutableSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongRequest;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeniusSongRetrieverImpl extends GeniusApiBase implements GeniusSongRetriever {
  private static final Logger LOGGER = LogManager.getLogger(GeniusSongRetrieverImpl.class);

  private final ObjectMapper objectMapper;

  private final HttpClient.Builder httpClientBuilder;

  @Autowired
  GeniusSongRetrieverImpl(ObjectMapper objectMapper, HttpClient.Builder httpClientBuilder) {
    this.objectMapper = objectMapper;
    this.httpClientBuilder = httpClientBuilder;
  }

  @Override
  public SongReply get(SongRequest songRequest) throws IOException, InterruptedException {
    final ImmutableSongReply.Builder songReply = ImmutableSongReply.builder();

    try (final HttpClient client = httpClientBuilder.build()) {
      for (final Long songId : songRequest.getSongIds()) {
        final HttpResponse<String> songResponse =
            client.send(
                HttpRequest.newBuilder()
                    .header("Authorization", String.format("Bearer %s", getAccessToken()))
                    .uri(
                        UriComponentsBuilder.newInstance()
                            .scheme("https")
                            .host("api.genius.com")
                            .pathSegment("songs", "{songId}")
                            .build(songId))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString());

        if (songResponse.statusCode() != 200) {
          LOGGER.warn(
              "Something went wrong when getting songId {}: {}. This song will not be returned.",
              songId,
              songResponse.body());
        } else {
          songReply.putSongReplies(
              songId, objectMapper.readValue(songResponse.body(), GeniusSongReply.class));
        }
      }
    }

    return songReply.build();
  }
}
