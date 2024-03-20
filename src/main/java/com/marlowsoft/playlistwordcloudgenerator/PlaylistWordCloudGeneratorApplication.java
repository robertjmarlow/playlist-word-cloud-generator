package com.marlowsoft.playlistwordcloudgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequestTrack;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import com.marlowsoft.playlistwordcloudgenerator.util.LyricsMassagingUtils;
import com.marlowsoft.playlistwordcloudgenerator.wordcloud.WordCloudGenerator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaylistWordCloudGeneratorApplication {
  private static final Logger LOGGER =
      LogManager.getLogger(PlaylistWordCloudGeneratorApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(PlaylistWordCloudGeneratorApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      ConfigurableApplicationContext configurableApplicationContext) {
    return new CommandLineRunner() {
      @Autowired ObjectMapper objectMapper;

      @Autowired PlaylistRetriever<Playlist, String> playlistRetriever;

      @Autowired LyricsRetriever<LyricsResponse, LyricsRequest> lyricsRetriever;

      @Autowired WordCloudGenerator wordCloudGenerator;

      @Override
      public void run(String... args) throws Exception {
        // get all the tracks in the playlist
        final Playlist playlist = playlistRetriever.getPlaylist(args[0]);

        LOGGER.info("I found playlist: \"{}\"", playlist.getName());

        // get lyrics for every track
        final ImmutableLyricsRequest.Builder lyricsRequestBuilder =
            ImmutableLyricsRequest.builder();
        for (final Playlist.TrackItem trackItem : playlist.getTracks().getTrackItems()) {
          lyricsRequestBuilder.addLyricsRequestTracks(
              ImmutableLyricsRequestTrack.builder()
                  .artist(trackItem.getTrackObject().getArtists().getFirst().getName())
                  .song(trackItem.getTrackObject().getName())
                  .build());
        }
        final LyricsResponse lyricsResponse =
            lyricsRetriever.getLyrics(lyricsRequestBuilder.build());

        // smash all the lyrics into a single list
        LOGGER.info("Messing around with the lyrics a bit...");
        List<String> lyrics =
            lyricsResponse.getLyricsResponseTracks().stream()
                .map(LyricsResponse.LyricsResponseTrack::getLyrics)
                .collect(Collectors.toList());

        // get rid of section annotations
        lyrics = LyricsMassagingUtils.removeSectionAnnotations(lyrics);

        // get rid of "boring words"
        lyrics = LyricsMassagingUtils.removeBoringWords(lyrics, objectMapper);

        // generate the word cloud
        LOGGER.info("Generating the word cloud");
        wordCloudGenerator.generate(
            lyrics, String.format("build/wordcloud-%s.png", playlist.getName()));

        System.exit(SpringApplication.exit(configurableApplicationContext));
      }
    };
  }
}
