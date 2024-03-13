package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeniusSongSearch {
  private static final Logger LOGGER = LogManager.getLogger(GeniusSongSearch.class);

  private final ObjectMapper objectMapper;

  @Autowired
  GeniusSongSearch(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
}
