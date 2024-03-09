package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutablePlaylist.class)
public abstract class Playlist {
  public abstract String getName();
}
