package com.marlowsoft.playlistwordcloudgenerator.lyrics;

import java.io.IOException;

public interface LyricsRetriever<T, V> {
  T getLyrics(V request) throws IOException, InterruptedException;
}
