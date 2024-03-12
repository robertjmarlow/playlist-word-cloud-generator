package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LyricsResponse {
  List<String> getLyrics();
}
