package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequestTrack;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.GeniusSearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchRequestItem;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.ImmutableSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeniusLyricsRetrieverTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  private GeniusLyricsRetriever geniusLyricsRetriever;

  @Mock private GeniusSongSearch geniusSongSearch;

  @Mock private GeniusSongRetriever geniusSongRetriever;

  @Mock private ConnectionRetriever connectionRetriever;

  @BeforeEach
  void beforeEach() {
    geniusLyricsRetriever =
        new GeniusLyricsRetriever(geniusSongSearch, geniusSongRetriever, connectionRetriever);
  }

  @Test
  void getLyrics(@Mock Connection connection)
      throws IOException, InterruptedException, URISyntaxException {
    final LyricsRequest lyricsRequest =
        ImmutableLyricsRequest.builder()
            .addLyricsRequestTracks(
                ImmutableLyricsRequestTrack.builder()
                    .song("Ghost of perdition")
                    .artist("Opeth")
                    .build(),
                ImmutableLyricsRequestTrack.builder()
                    .song("Dying Star")
                    .artist("Periphery")
                    .build(),
                ImmutableLyricsRequestTrack.builder()
                    .song("The World Breathes with Me")
                    .artist("Caligula's Horse")
                    .build())
            .build();

    final GeniusSearchReply geniusSearchReply1 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/ghost-of-perdition.json"),
            GeniusSearchReply.class);
    final GeniusSearchReply geniusSearchReply2 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/dying-star.json"), GeniusSearchReply.class);
    final GeniusSearchReply geniusSearchReply3 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/the-world-breathes-with-me.json"),
            GeniusSearchReply.class);

    when(geniusSongSearch.search(any(SearchRequest.class)))
        .thenReturn(
            ImmutableSearchReply.builder()
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("Ghost of perdition")
                        .artist("Opeth")
                        .build(),
                    geniusSearchReply1)
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("Dying Star")
                        .artist("Periphery")
                        .build(),
                    geniusSearchReply2)
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("The World Breathes with Me")
                        .artist("Caligula's Horse")
                        .build(),
                    geniusSearchReply3)
                .build());

    final GeniusSongReply geniusSongReply1 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/song/ghost-of-perdition.json"), GeniusSongReply.class);
    final GeniusSongReply geniusSongReply2 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/song/dying-star.json"), GeniusSongReply.class);
    final GeniusSongReply geniusSongReply3 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/song/the-world-breathes-with-me.json"),
            GeniusSongReply.class);

    when(geniusSongRetriever.get(any(SongRequest.class)))
        .thenReturn(
            ImmutableSongReply.builder()
                .putSongReplies(1015520, geniusSongReply1)
                .putSongReplies(8652014, geniusSongReply2)
                .putSongReplies(9715712, geniusSongReply3)
                .build());

    final Document document1 =
        Jsoup.parse(
            new File(
                Resources.getResource(
                        "genius/site/Opeth – Ghost of Perdition Lyrics Genius Lyrics.html")
                    .toURI()),
            StandardCharsets.UTF_8.name(),
            "https://genius.com/Opeth-ghost-of-perdition-lyrics");
    final Document document2 =
        Jsoup.parse(
            new File(
                Resources.getResource(
                        "genius/site/Periphery – Dying Star Lyrics Genius Lyrics.html")
                    .toURI()),
            StandardCharsets.UTF_8.name(),
            "https://genius.com/Periphery-dying-star-lyrics");
    final Document document3 =
        Jsoup.parse(
            new File(
                Resources.getResource(
                        "genius/site/Caligula's Horse – The World Breathes with Me Lyrics Genius Lyrics.html")
                    .toURI()),
            StandardCharsets.UTF_8.name(),
            "https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics");

    when(connectionRetriever.get(any(URL.class))).thenReturn(connection);
    when(connection.get()).thenReturn(document1, document2, document3);

    final LyricsResponse lyricsResponse = geniusLyricsRetriever.getLyrics(lyricsRequest);

    assertEquals(3, lyricsResponse.getLyricsResponseTracks().size());

    assertEquals(1015520, lyricsResponse.getLyricsResponseTracks().get(0).getSong().getId());
    assertEquals(
        URI.create("https://genius.com/Opeth-ghost-of-perdition-lyrics").toURL(),
        lyricsResponse.getLyricsResponseTracks().get(0).getSong().getUrl());
    assertTrue(
        lyricsResponse.getLyricsResponseTracks().get(0).getLyrics().contains("Ghost of perdition"));
    assertEquals(8652014, lyricsResponse.getLyricsResponseTracks().get(1).getSong().getId());
    assertEquals(
        URI.create("https://genius.com/Periphery-dying-star-lyrics").toURL(),
        lyricsResponse.getLyricsResponseTracks().get(1).getSong().getUrl());
    assertTrue(
        lyricsResponse
            .getLyricsResponseTracks()
            .get(1)
            .getLyrics()
            .contains("You're as persistent as a dying star"));
    assertEquals(9715712, lyricsResponse.getLyricsResponseTracks().get(2).getSong().getId());
    assertEquals(
        URI.create("https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics").toURL(),
        lyricsResponse.getLyricsResponseTracks().get(2).getSong().getUrl());
    assertTrue(
        lyricsResponse
            .getLyricsResponseTracks()
            .get(2)
            .getLyrics()
            .contains("I breathe and the world breathes with me"));
  }

  @Test
  void getLyricsWithNoResultsForASearch(@Mock Connection connection)
      throws IOException, InterruptedException, URISyntaxException {
    final LyricsRequest lyricsRequest =
        ImmutableLyricsRequest.builder()
            .addLyricsRequestTracks(
                ImmutableLyricsRequestTrack.builder()
                    .song("Ghost of perdition")
                    .artist("Opeth")
                    .build(),
                ImmutableLyricsRequestTrack.builder()
                    .song("Dying Star")
                    .artist("Periphery")
                    .build(),
                ImmutableLyricsRequestTrack.builder()
                    .song("The World Breathes with Me")
                    .artist("Caligula's Horse")
                    .build())
            .build();

    final GeniusSearchReply geniusSearchReply1 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/ghost-of-perdition.json"),
            GeniusSearchReply.class);
    final GeniusSearchReply geniusSearchReply2 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/no-results.json"), GeniusSearchReply.class);
    final GeniusSearchReply geniusSearchReply3 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/search/the-world-breathes-with-me.json"),
            GeniusSearchReply.class);

    when(geniusSongSearch.search(any(SearchRequest.class)))
        .thenReturn(
            ImmutableSearchReply.builder()
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("Ghost of perdition")
                        .artist("Opeth")
                        .build(),
                    geniusSearchReply1)
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("Dying Star")
                        .artist("Periphery")
                        .build(),
                    geniusSearchReply2)
                .putSearchResults(
                    ImmutableSearchRequestItem.builder()
                        .track("The World Breathes with Me")
                        .artist("Caligula's Horse")
                        .build(),
                    geniusSearchReply3)
                .build());

    final GeniusSongReply geniusSongReply1 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/song/ghost-of-perdition.json"), GeniusSongReply.class);
    final GeniusSongReply geniusSongReply2 =
        OBJECT_MAPPER.readValue(
            Resources.getResource("genius/song/the-world-breathes-with-me.json"),
            GeniusSongReply.class);

    when(geniusSongRetriever.get(any(SongRequest.class)))
        .thenReturn(
            ImmutableSongReply.builder()
                .putSongReplies(1015520, geniusSongReply1)
                .putSongReplies(9715712, geniusSongReply2)
                .build());

    final Document document1 =
        Jsoup.parse(
            new File(
                Resources.getResource(
                        "genius/site/Opeth – Ghost of Perdition Lyrics Genius Lyrics.html")
                    .toURI()),
            StandardCharsets.UTF_8.name(),
            "https://genius.com/Opeth-ghost-of-perdition-lyrics");
    final Document document2 =
        Jsoup.parse(
            new File(
                Resources.getResource(
                        "genius/site/Caligula's Horse – The World Breathes with Me Lyrics Genius Lyrics.html")
                    .toURI()),
            StandardCharsets.UTF_8.name(),
            "https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics");

    when(connectionRetriever.get(any(URL.class))).thenReturn(connection);
    when(connection.get()).thenReturn(document1, document2);

    final LyricsResponse lyricsResponse = geniusLyricsRetriever.getLyrics(lyricsRequest);

    assertEquals(2, lyricsResponse.getLyricsResponseTracks().size());

    assertEquals(1015520, lyricsResponse.getLyricsResponseTracks().get(0).getSong().getId());
    assertEquals(
        URI.create("https://genius.com/Opeth-ghost-of-perdition-lyrics").toURL(),
        lyricsResponse.getLyricsResponseTracks().get(0).getSong().getUrl());
    assertTrue(
        lyricsResponse.getLyricsResponseTracks().get(0).getLyrics().contains("Ghost of perdition"));
    assertEquals(9715712, lyricsResponse.getLyricsResponseTracks().get(1).getSong().getId());
    assertEquals(
        URI.create("https://genius.com/Caligulas-horse-the-world-breathes-with-me-lyrics").toURL(),
        lyricsResponse.getLyricsResponseTracks().get(1).getSong().getUrl());
    assertTrue(
        lyricsResponse
            .getLyricsResponseTracks()
            .get(1)
            .getLyrics()
            .contains("I breathe and the world breathes with me"));
  }
}
