package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableToken.class)
public interface Token {
  @JsonProperty("access_token")
  String getAccessToken();

  @JsonProperty("token_type")
  String getTokenType();

  @JsonProperty("expires_in")
  long getExpiresIn();
}
