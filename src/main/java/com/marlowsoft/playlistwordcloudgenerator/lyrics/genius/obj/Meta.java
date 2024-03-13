package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableMeta.class)
public interface Meta {
  @JsonProperty("status")
  int getStatus();

  @JsonProperty("message")
  @Nullable
  String getMessage();
}
