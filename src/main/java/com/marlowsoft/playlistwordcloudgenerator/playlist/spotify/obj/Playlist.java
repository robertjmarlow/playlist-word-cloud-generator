package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.annotation.Nullable;
import java.net.URI;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutablePlaylist.class)
public abstract class Playlist {
  @JsonProperty("description")
  public abstract String getDescription();

  @JsonProperty("name")
  public abstract String getName();

  @JsonProperty("href")
  public abstract URL getHref();

  @JsonProperty("id")
  public abstract String getId();

  @JsonProperty("tracks")
  public abstract Tracks getTracks();

  @JsonProperty("uri")
  public abstract URI getUri();

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTracks.class)
  public abstract static class Tracks {
    @JsonProperty("href")
    public abstract URL getHref();

    @JsonProperty("limit")
    public abstract int getLimit();

    @JsonProperty("next")
    @Nullable
    public abstract URL getNext();

    @JsonProperty("offset")
    public abstract int getOffset();

    @JsonProperty("previous")
    @Nullable
    public abstract URL getPrevious();

    @JsonProperty("total")
    public abstract int getTotal();

    @JsonProperty("items")
    public abstract List<TrackItem> getTrackItems();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackItem.class)
  public abstract static class TrackItem {
    @JsonProperty("added_at")
    public abstract ZonedDateTime getAddedAt();

    @JsonProperty("track")
    public abstract TrackObject getTrackObject();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackObject.class)
  public abstract static class TrackObject {
    @JsonProperty("album")
    public abstract TrackAlbum getAlbum();

    @JsonProperty("artists")
    public abstract List<TrackArtist> getArtists();

    @JsonProperty("duration_ms")
    public abstract int getDuration();

    @JsonProperty("href")
    public abstract URL getHref();

    @JsonProperty("id")
    public abstract String getId();

    @JsonProperty("name")
    public abstract String getName();

    @JsonProperty("uri")
    public abstract URI getUri();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackAlbum.class)
  public abstract static class TrackAlbum {
    @JsonProperty("href")
    public abstract URL getHref();

    @JsonProperty("id")
    public abstract String getId();

    @JsonProperty("name")
    public abstract String getName();

    @JsonProperty("release_date")
    public abstract String getReleaseDate();

    // TODO add release_date_precision?

    @JsonProperty("uri")
    public abstract URI getUri();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackArtist.class)
  public abstract static class TrackArtist {
    @JsonProperty("genres")
    public abstract List<String> getGenres();

    @JsonProperty("href")
    public abstract URL getHref();

    @JsonProperty("id")
    public abstract String getId();

    @JsonProperty("name")
    public abstract String getName();

    @JsonProperty("uri")
    public abstract URI getUri();
  }
}
