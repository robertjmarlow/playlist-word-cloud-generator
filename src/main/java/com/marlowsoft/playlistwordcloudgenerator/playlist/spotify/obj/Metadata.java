package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.net.URL;

public interface Metadata {
  @JsonProperty("href")
  URL getHref();

  @JsonProperty("id")
  String getId();

  @JsonProperty("uri")
  URI getUri();
}
