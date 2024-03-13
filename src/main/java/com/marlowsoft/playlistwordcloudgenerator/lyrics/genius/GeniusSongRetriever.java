package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.song.SongRequest;
import java.io.IOException;

public interface GeniusSongRetriever {
  SongReply get(SongRequest songRequest) throws IOException, InterruptedException;
}
