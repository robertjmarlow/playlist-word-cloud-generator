package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.annotation.Nullable;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutablePlaylist.class)
public interface Playlist extends Metadata {
  @JsonProperty("description")
  String getDescription();

  @JsonProperty("name")
  String getName();

  @JsonProperty("tracks")
  Tracks getTracks();

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTracks.class)
  interface Tracks {
    @JsonProperty("href")
    URL getHref();

    @JsonProperty("limit")
    int getLimit();

    @JsonProperty("next")
    @Nullable
    URL getNext();

    @JsonProperty("offset")
    int getOffset();

    @JsonProperty("previous")
    @Nullable
    URL getPrevious();

    @JsonProperty("total")
    int getTotal();

    @JsonProperty("items")
    List<TrackItem> getTrackItems();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackItem.class)
  interface TrackItem {
    @JsonProperty("added_at")
    ZonedDateTime getAddedAt();

    @JsonProperty("track")
    TrackObject getTrackObject();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackObject.class)
  interface TrackObject extends Metadata {
    @JsonProperty("album")
    TrackAlbum getAlbum();

    @JsonProperty("artists")
    List<TrackArtist> getArtists();

    @JsonProperty("duration_ms")
    int getDuration();

    @JsonProperty("name")
    String getName();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackAlbum.class)
  interface TrackAlbum extends Metadata {
    @JsonProperty("name")
    String getName();

    @JsonProperty("release_date")
    String getReleaseDate();

    // TODO add release_date_precision?
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableTrackArtist.class)
  interface TrackArtist extends Metadata {
    @JsonProperty("genres")
    List<String> getGenres();

    @JsonProperty("name")
    String getName();
  }
}
