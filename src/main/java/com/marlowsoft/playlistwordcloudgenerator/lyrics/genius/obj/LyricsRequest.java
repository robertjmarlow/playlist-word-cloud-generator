package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import org.immutables.value.Value;

@Value.Immutable
public interface LyricsRequest {
  String getArtist();

  String getSong();
}
