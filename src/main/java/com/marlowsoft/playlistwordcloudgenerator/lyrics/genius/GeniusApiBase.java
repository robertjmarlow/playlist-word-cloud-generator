package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class GeniusApiBase {
  private static final Logger LOGGER = LogManager.getLogger(GeniusApiBase.class);

  private static final String CLIENT_ACCESS_TOKEN;

  static {
    final Map<String, String> environmentVars = System.getenv();

    CLIENT_ACCESS_TOKEN = environmentVars.getOrDefault("GENIUS_CLIENT_ACCESS_TOKEN", "");

    if (CLIENT_ACCESS_TOKEN.isEmpty()) {
      LOGGER.warn(
          "GENIUS_CLIENT_ACCESS_TOKEN was not set. This will probably result in 401s when calling the Genius API. Set this in the environment variables.");
    }
  }

  protected String getAccessToken() {
    return CLIENT_ACCESS_TOKEN;
  }
}
