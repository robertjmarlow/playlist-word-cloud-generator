package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.ImmutableSongRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongReply;
import java.io.IOException;
import java.net.http.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeniusLyricsRetriever extends GeniusApiBase
    implements LyricsRetriever<LyricsResponse, LyricsRequest> {
  private static final Logger LOGGER = LogManager.getLogger(GeniusLyricsRetriever.class);

  private final HttpClient.Builder httpClientBuilder;

  private final GeniusSongSearch geniusSongSearch;

  private final GeniusSongRetriever geniusSongRetriever;

  @Autowired
  GeniusLyricsRetriever(
      HttpClient.Builder httpClientBuilder,
      GeniusSongSearch geniusSongSearch,
      GeniusSongRetriever geniusSongRetriever) {
    this.httpClientBuilder = httpClientBuilder;
    this.geniusSongSearch = geniusSongSearch;
    this.geniusSongRetriever = geniusSongRetriever;
  }

  @Override
  public LyricsResponse getLyrics(LyricsRequest request) throws IOException, InterruptedException {
    final SongReply songReply =
        geniusSongRetriever.get(
            ImmutableSongRequest.builder().addSongIds(1015520, 8652014, 9715712).build());

    LOGGER.info("i got this back from the song retriever: {}", songReply);

    return ImmutableLyricsResponse.builder()
        .addLyrics("syrup sandwiches", "syrup, syrup, syrup sandwiches")
        .build();
  }
}
