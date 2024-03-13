package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.Meta;
import java.net.URL;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableGeniusSongReply.class)
public interface GeniusSongReply {
  @JsonProperty("meta")
  Meta getMeta();

  @JsonProperty("response")
  Response getResponse();

  @Value.Immutable
  @JsonDeserialize(as = ImmutableResponse.class)
  interface Response {
    @JsonProperty("song")
    Song getSong();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableSong.class)
  interface Song {
    @JsonProperty("full_title")
    String getFullTitle();

    @JsonProperty("title")
    String getTitle();

    @JsonProperty("artist_names")
    String getArtistNames();

    @JsonProperty("api_path")
    String getApiPath();

    @JsonProperty("url")
    URL getUrl();

    @JsonProperty("id")
    long getId();
  }
}
