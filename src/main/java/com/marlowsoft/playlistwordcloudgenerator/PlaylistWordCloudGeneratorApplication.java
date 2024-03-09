package com.marlowsoft.playlistwordcloudgenerator;

import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
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

      @Override
      public void run(String... args) throws Exception {
        final Playlist playlist = playlistRetriever.getPlaylist("lol1234");

        LOGGER.info("i got this playlist back: {}", playlist);

        System.exit(SpringApplication.exit(configurableApplicationContext));
      }
    };
  }
}
