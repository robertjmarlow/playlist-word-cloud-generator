package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LyricsRequest {
  List<LyricsRequestTrack> getLyricsRequestTracks();

  @Value.Immutable
  interface LyricsRequestTrack {
    String getArtist();

    String getSong();
  }
}
