package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.Meta;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableGeniusSearchReply.class)
public interface GeniusSearchReply {
  @JsonProperty("meta")
  Meta getMeta();

  @JsonProperty("response")
  Response getResponse();

  @Value.Immutable
  @JsonDeserialize(as = ImmutableResponse.class)
  interface Response {
    @JsonProperty("hits")
    List<Hit> getHits();
  }

  @Value.Immutable
  @JsonDeserialize(as = ImmutableHit.class)
  interface Hit {
    @JsonProperty("index")
    String getIndex();

    @JsonProperty("type")
    String getType();

    @JsonProperty("result")
    GeniusSongReply.Song getResult();
  }
}
