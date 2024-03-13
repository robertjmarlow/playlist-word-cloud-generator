package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song;

import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface SongRequest {
  Set<Long> getSongIds();
}
