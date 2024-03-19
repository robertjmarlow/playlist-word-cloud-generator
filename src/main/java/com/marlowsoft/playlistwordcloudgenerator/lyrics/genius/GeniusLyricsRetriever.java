package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.ImmutableSearchRequestItem;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.ImmutableSongRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongReply;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeniusLyricsRetriever extends GeniusApiBase
    implements LyricsRetriever<LyricsResponse, LyricsRequest> {
  private static final Logger LOGGER = LogManager.getLogger(GeniusLyricsRetriever.class);

  private static final String USER_AGENT =
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0";

  private final GeniusSongSearch geniusSongSearch;

  private final GeniusSongRetriever geniusSongRetriever;

  @Autowired
  GeniusLyricsRetriever(
      GeniusSongSearch geniusSongSearch, GeniusSongRetriever geniusSongRetriever) {
    this.geniusSongSearch = geniusSongSearch;
    this.geniusSongRetriever = geniusSongRetriever;
  }

  @Override
  public LyricsResponse getLyrics(LyricsRequest request) throws IOException, InterruptedException {
    final ImmutableLyricsResponse.Builder responseBuilder = ImmutableLyricsResponse.builder();

    final ImmutableSearchRequest.Builder searchRequest = ImmutableSearchRequest.builder();

    // get all the lyrics to search for
    for (final LyricsRequest.LyricsRequestTrack lyricsRequestTrack :
        request.getLyricsRequestTracks()) {
      // search for the song via artist + track
      searchRequest.addSearchRequestItems(
          ImmutableSearchRequestItem.builder()
              .artist(lyricsRequestTrack.getArtist())
              .track(lyricsRequestTrack.getSong())
              .build());
    }

    // search for the tracks
    final SearchReply searchReply = geniusSongSearch.search(searchRequest.build());

    // it's a pretty good assumption the first song in the list is the one we're looking for
    final List<Long> songIds =
        searchReply.getSearchResults().values().stream()
            .filter(geniusSearchReply -> !geniusSearchReply.getResponse().getHits().isEmpty())
            .map(
                geniusSearchReply ->
                    geniusSearchReply.getResponse().getHits().getFirst().getResult().getId())
            .toList();

    // get all the songs
    final SongReply songReply =
        geniusSongRetriever.get(ImmutableSongRequest.builder().songIds(songIds).build());

    // for each song, scrape the lyrics page for the lyrics
    for (final GeniusSongReply geniusSongReply : songReply.getSongReplies().values()) {
      LOGGER.info(
          "Scraping the Genius page for \"{}\" for lyrics",
          geniusSongReply.getResponse().getSong().getFullTitle());

      final Document doc =
          Jsoup.connect(geniusSongReply.getResponse().getSong().getUrl().toString())
              .userAgent(USER_AGENT)
              .get();

      final Elements lyrics = doc.select("div[class^=Lyrics__Container]");

      responseBuilder.song(geniusSongReply.getResponse().getSong()).addLyrics(lyrics.text());
    }

    return responseBuilder.build();
  }
}
