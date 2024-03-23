package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.GeniusSearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableGeniusSearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchRequest;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeniusSongSearchImpl extends GeniusApiBase implements GeniusSongSearch {
  private static final Logger LOGGER = LogManager.getLogger(GeniusSongSearchImpl.class);

  // Genius puts these in the search results in their titles.
  // I'm honestly not sure what their significance is.
  private static final String WEIRD_SEPARATOR = "\u00A0";

  private final ObjectMapper objectMapper;

  private final HttpClient.Builder httpClientBuilder;

  @Autowired
  GeniusSongSearchImpl(ObjectMapper objectMapper, HttpClient.Builder httpClientBuilder) {
    this.objectMapper = objectMapper;
    this.httpClientBuilder = httpClientBuilder;
  }

  @Override
  public SearchReply search(SearchRequest searchRequest) throws IOException, InterruptedException {
    final ImmutableSearchReply.Builder searchReply = ImmutableSearchReply.builder();

    try (final HttpClient client = httpClientBuilder.build()) {
      for (final SearchRequest.SearchRequestItem searchRequestItem :
          searchRequest.getSearchRequestItems()) {
        final String search =
            String.format("%s %s", searchRequestItem.getArtist(), searchRequestItem.getTrack());

        LOGGER.info(
            "Searching for: {} - \"{}\"",
            searchRequestItem.getArtist(),
            searchRequestItem.getTrack());

        final HttpResponse<String> searchResponse =
            client.send(
                HttpRequest.newBuilder()
                    .header("Authorization", String.format("Bearer %s", getAccessToken()))
                    .uri(
                        UriComponentsBuilder.newInstance()
                            .scheme("https")
                            .host("api.genius.com")
                            .path("search")
                            .queryParam("q", search)
                            .build()
                            .toUri())
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString());

        if (searchResponse.statusCode() != 200) {
          LOGGER.warn(
              "Something went wrong when searching for \"{}\": {}. This song will not be returned.",
              search,
              searchResponse.body());
        } else {
          final GeniusSearchReply geniusSearchReply =
              objectMapper.readValue(searchResponse.body(), GeniusSearchReply.class);

          // the search is really robust. like, TOO robust sometimes.
          // remove stuff that doesn't even have the artist in the full title.
          final List<GeniusSearchReply.Hit> filteredHits =
              geniusSearchReply.getResponse().getHits().stream()
                  .filter(
                      hit ->
                          hit.getResult()
                              .getFullTitle()
                              .replace(WEIRD_SEPARATOR, " ")
                              .contains(searchRequestItem.getArtist()))
                  .toList();

          final GeniusSearchReply filteredGeniusSearchReply =
              ImmutableGeniusSearchReply.builder()
                  .from(geniusSearchReply)
                  .response(ImmutableResponse.builder().addAllHits(filteredHits).build())
                  .build();

          searchReply.putSearchResults(searchRequestItem, filteredGeniusSearchReply);
        }
      }
    }

    return searchReply.build();
  }
}
