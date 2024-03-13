package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.ImmutableSongRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongReply;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeniusSongRetrieverTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  private GeniusSongRetriever geniusSongRetriever;

  @Mock private HttpClient.Builder httpClientBuilder;

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<String> httpResponse;

  @BeforeEach
  void beforeEach() {
    geniusSongRetriever = new GeniusSongRetrieverImpl(OBJECT_MAPPER, httpClientBuilder);
  }

  @Test
  void getSongsAllSuccess() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(200);
    when(httpResponse.body())
        .thenReturn(
            Resources.toString(
                Resources.getResource("genius/ghost-of-perdition.json"), StandardCharsets.UTF_8),
            Resources.toString(
                Resources.getResource("genius/dying-star.json"), StandardCharsets.UTF_8),
            Resources.toString(
                Resources.getResource("genius/the-world-breathes-with-me.json"),
                StandardCharsets.UTF_8));

    final long songId1 = 1015520; // Ghost of Perdition
    final long songId2 = 8652014; // Dying Star
    final long songId3 = 9715712; // The World Breathes with Me
    final List<Long> songIds = Arrays.asList(songId1, songId2, songId3);

    final SongReply songReply =
        geniusSongRetriever.get(ImmutableSongRequest.builder().songIds(songIds).build());

    assertTrue(songReply.getSongReplies().keySet().containsAll(songIds));

    final GeniusSongReply.Song song1 =
        songReply.getSongReplies().get(songId1).getResponse().getSong();
    final GeniusSongReply.Song song2 =
        songReply.getSongReplies().get(songId2).getResponse().getSong();
    final GeniusSongReply.Song song3 =
        songReply.getSongReplies().get(songId3).getResponse().getSong();

    assertEquals("Opeth", song1.getArtistNames());
    assertEquals("Ghost of Perdition", song1.getTitle());
    assertEquals(songId1, song1.getId());
    assertEquals(
        URI.create("https://genius.com/Opeth-ghost-of-perdition-lyrics").toURL(), song1.getUrl());
    assertEquals("Periphery", song2.getArtistNames());
    assertEquals("Dying Star", song2.getTitle());
    assertEquals(songId2, song2.getId());
    assertEquals(
        URI.create("https://genius.com/Periphery-dying-star-lyrics").toURL(), song2.getUrl());
    assertEquals("Caligula's Horse", song3.getArtistNames());
    assertEquals("The World Breathes with Me", song3.getTitle());
    assertEquals(songId3, song3.getId());
    assertEquals(
        URI.create("https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics").toURL(),
        song3.getUrl());
  }

  @Test
  void getSongsMostlySuccess() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(200, 404, 200);
    when(httpResponse.body())
        .thenReturn(
            Resources.toString(
                Resources.getResource("genius/ghost-of-perdition.json"), StandardCharsets.UTF_8),
            Resources.toString(Resources.getResource("genius/404.json"), StandardCharsets.UTF_8),
            Resources.toString(
                Resources.getResource("genius/the-world-breathes-with-me.json"),
                StandardCharsets.UTF_8));

    final long songId1 = 1015520; // Ghost of Perdition
    final long songId2 = 0; // doesn't exist :(
    final long songId3 = 9715712; // The World Breathes with Me
    final List<Long> songIds = Arrays.asList(songId1, songId2, songId3);
    final List<Long> validSongIds = Arrays.asList(songId1, songId3);

    final SongReply songReply =
        geniusSongRetriever.get(ImmutableSongRequest.builder().songIds(songIds).build());

    assertTrue(songReply.getSongReplies().keySet().containsAll(validSongIds));
    assertFalse(songReply.getSongReplies().containsKey(songId2));

    final GeniusSongReply.Song song1 =
        songReply.getSongReplies().get(songId1).getResponse().getSong();
    final GeniusSongReply.Song song3 =
        songReply.getSongReplies().get(songId3).getResponse().getSong();

    assertEquals("Opeth", song1.getArtistNames());
    assertEquals("Ghost of Perdition", song1.getTitle());
    assertEquals(songId1, song1.getId());
    assertEquals(
        URI.create("https://genius.com/Opeth-ghost-of-perdition-lyrics").toURL(), song1.getUrl());
    assertEquals("Caligula's Horse", song3.getArtistNames());
    assertEquals("The World Breathes with Me", song3.getTitle());
    assertEquals(songId3, song3.getId());
    assertEquals(
        URI.create("https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics").toURL(),
        song3.getUrl());
  }
}
