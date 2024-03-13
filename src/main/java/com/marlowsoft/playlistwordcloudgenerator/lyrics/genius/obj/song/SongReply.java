package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song;

import java.util.Map;
import org.immutables.value.Value;

@Value.Immutable
public interface SongReply {
  Map<Long, GeniusSongReply> getSongReplies();
}
