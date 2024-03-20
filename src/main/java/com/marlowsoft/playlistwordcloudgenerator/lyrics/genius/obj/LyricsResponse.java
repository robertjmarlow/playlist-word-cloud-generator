package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.GeniusSongReply;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LyricsResponse {
  List<LyricsResponseTrack> getLyricsResponseTracks();

  @Value.Immutable
  interface LyricsResponseTrack {
    GeniusSongReply.Song getSong();

    String getLyrics();
  }
}
