package com.marlowsoft.playlistwordcloudgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
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
import com.marlowsoft.playlistwordcloudgenerator.wordcloud.obj.BoringWords;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
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
      @Autowired ObjectMapper objectMapper;

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
        lyrics = removeBoringWords(lyrics);

        LOGGER.info("the more interesting lyrics are: {}", lyrics);

        // generate the word cloud
        createWordCloud(lyrics, "build/wordcloud_circle_sqrt_font.png");

        System.exit(SpringApplication.exit(configurableApplicationContext));
      }

      private void createWordCloud(final List<String> lyrics, final String outputFileName) {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
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
        wordCloud.build(frequencyAnalyzer.load(lyrics));
        wordCloud.writeToFile(outputFileName);
      }

      private List<String> removeSectionAnnotations(final List<String> lyrics) {
        final Pattern sectionAnnotationPattern = Pattern.compile("\\[.*\\]");

        final ImmutableList.Builder<String> updatedLyricsBuilder =
            ImmutableList.builderWithExpectedSize(lyrics.size());

        for (final String songLyrics : lyrics) {
          updatedLyricsBuilder.add(sectionAnnotationPattern.matcher(songLyrics).replaceAll(""));
        }

        return updatedLyricsBuilder.build();
      }

      private List<String> removeBoringWords(final List<String> lyrics) throws IOException {
        final BoringWords boringWords =
            objectMapper.readValue(Resources.getResource("boring-words.json"), BoringWords.class);

        final Pattern boringWordsPattern =
            Pattern.compile(
                String.format("\\b(%s)\\b", Joiner.on('|').join(boringWords.getBoringWords())),
                Pattern.CASE_INSENSITIVE);

        final ImmutableList.Builder<String> updatedLyricsBuilder =
            ImmutableList.builderWithExpectedSize(lyrics.size());

        for (final String songLyrics : lyrics) {
          updatedLyricsBuilder.add(boringWordsPattern.matcher(songLyrics).replaceAll(""));
        }

        return updatedLyricsBuilder.build();
      }
    };
  }
}
