package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LyricsResponse {
  GeniusSongReply.Song getSong();

  List<String> getLyrics();
}
