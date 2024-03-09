package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableToken.class)
public abstract class Token {
  @JsonProperty("access_token")
  public abstract String getAccessToken();

  @JsonProperty("token_type")
  public abstract String getTokenType();

  @JsonProperty("expires_in")
  public abstract long getExpiresIn();
}
