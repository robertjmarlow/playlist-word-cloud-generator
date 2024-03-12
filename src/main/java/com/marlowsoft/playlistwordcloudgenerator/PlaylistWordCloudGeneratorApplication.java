package com.marlowsoft.playlistwordcloudgenerator;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.LyricsRetriever;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.ImmutableLyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsRequest;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.LyricsResponse;
import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import java.util.List;
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
      @Autowired PlaylistRetriever<Playlist, String> playlistRetriever;

      @Autowired LyricsRetriever<LyricsResponse, LyricsRequest> lyricsRetriever;

      @Override
      public void run(String... args) throws Exception {
        //        final Playlist playlist = playlistRetriever.getPlaylist("4H9YsKpBfjbnmOlyZIIlgy");

        //        LOGGER.info("i got this playlist back: {}", playlist);

        final LyricsResponse response =
            lyricsRetriever.getLyrics(
                ImmutableLyricsRequest.builder().artist("Kendrick Lamar").song("HUMBLE.").build());
        final List<String> lyrics = response.getLyrics();

        LOGGER.info("i got these lyrics back: {}", lyrics);

        System.exit(SpringApplication.exit(configurableApplicationContext));
      }
    };
  }
}
