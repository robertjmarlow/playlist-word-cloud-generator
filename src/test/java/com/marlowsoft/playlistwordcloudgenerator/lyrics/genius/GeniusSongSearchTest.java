package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchRequestItem;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
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
public class GeniusSongSearchTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  private GeniusSongSearch geniusSongSearch;

  @Mock private HttpClient.Builder httpClientBuilder;

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<String> httpResponse;

  @BeforeEach
  void beforeEach() {
    geniusSongSearch = new GeniusSongSearchImpl(OBJECT_MAPPER, httpClientBuilder);
  }

  @Test
  void getSongsSearchSuccess() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(200);
    when(httpResponse.body())
        .thenReturn(
            Resources.toString(
                Resources.getResource("genius/search/a-change-would-do-you-good.json"),
                StandardCharsets.UTF_8));

    final SearchRequest.SearchRequestItem searchRequestItem =
        ImmutableSearchRequestItem.builder()
            .track("A Change Would Do You Good")
            .artist("Sheryl Crow")
            .build();

    final SearchReply searchReply =
        geniusSongSearch.search(
            ImmutableSearchRequest.builder().addSearchRequestItems(searchRequestItem).build());

    assertTrue(searchReply.getSearchResults().containsKey(searchRequestItem));

    assertFalse(
        searchReply.getSearchResults().get(searchRequestItem).getResponse().getHits().isEmpty());

    final GeniusSongReply.Song song =
        searchReply
            .getSearchResults()
            .get(searchRequestItem)
            .getResponse()
            .getHits()
            .getFirst()
            .getResult();

    assertEquals("Sheryl Crow", song.getArtistNames());
    assertEquals("A Change Would Do You Good", song.getTitle());
    assertEquals(678224, song.getId());
  }

  @Test
  void getSongsSearch404() throws IOException, InterruptedException {
    when(httpClientBuilder.build()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    when(httpResponse.statusCode()).thenReturn(404);

    final SearchRequest.SearchRequestItem searchRequestItem =
        ImmutableSearchRequestItem.builder()
            .track("A Change Would Do You Good")
            .artist("Sheryl Crow")
            .build();

    final SearchReply searchReply =
        geniusSongSearch.search(
            ImmutableSearchRequest.builder().addSearchRequestItems(searchRequestItem).build());

    assertFalse(searchReply.getSearchResults().containsKey(searchRequestItem));
  }
}
