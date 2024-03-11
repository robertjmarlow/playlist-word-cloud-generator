package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutablePlaylist.class)
public abstract class Playlist {
  @JsonProperty("description")
  public abstract String getDescription();

  @JsonProperty("name")
  public abstract String getName();

  @JsonProperty("id")
  public abstract String getPlayListId();

  @JsonProperty("tracks")
  public abstract Tracks getTracks();

  @JsonProperty("uri")
  public abstract URI getUri();

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTracks.class)
  public abstract static class Tracks {
    @JsonProperty("href")
    public abstract URL getHref();

    @JsonProperty("items")
    public abstract List<TrackItem> getTrackItems();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackItem.class)
  public abstract static class TrackItem {
    @JsonProperty("track")
    public abstract TrackObject getTrackObject();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackObject.class)
  public abstract static class TrackObject {
    @JsonProperty("href")
    public abstract URL getHref();
  }
}
