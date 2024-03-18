package com.marlowsoft.playlistwordcloudgenerator;

import com.google.common.collect.ImmutableList;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequestTrack;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.regex.Pattern;
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
      private static final Pattern SECTION_ANNOTATION_PATTERN = Pattern.compile("\\[.*\\]");

      @Autowired PlaylistRetriever<Playlist, String> playlistRetriever;

      @Autowired LyricsRetriever<LyricsResponse, LyricsRequest> lyricsRetriever;

      @Override
      public void run(String... args) throws Exception {
        // get all the tracks in the playlist
        final Playlist playlist = playlistRetriever.getPlaylist("4H9YsKpBfjbnmOlyZIIlgy");

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

        List<String> lyrics = lyricsRetriever.getLyrics(lyricsRequestBuilder.build()).getLyrics();

        LOGGER.info("i got these lyrics back: {}", lyrics);

        // get rid of section annotations
        lyrics = removeSectionAnnotations(lyrics);

        // get rid of "boring words"

        // generate the word cloud
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        final WordCloud wordCloud = getWordCloud();
        wordCloud.build(frequencyAnalyzer.load(lyrics));
        wordCloud.writeToFile("build/wordcloud_circle_sqrt_font.png");

        System.exit(SpringApplication.exit(configurableApplicationContext));
      }

      private static WordCloud getWordCloud() {
        final WordCloud wordCloud =
            new WordCloud(new Dimension(600, 600), CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(
            new ColorPalette(
                new Color(0x4055F1),
                new Color(0x408DF1),
                new Color(0x40AAF1),
                new Color(0x40C5F1),
                new Color(0x40D3F1),
                new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        return wordCloud;
      }

      private static List<String> removeSectionAnnotations(final List<String> lyrics) {
        ImmutableList.Builder<String> updatedLyrics = ImmutableList.builder();

        for (final String songLyrics : lyrics) {
          updatedLyrics.add(SECTION_ANNOTATION_PATTERN.matcher(songLyrics).replaceAll(""));
        }

        return updatedLyrics.build();
      }
    };
  }
}
