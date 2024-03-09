package com.marlowsoft.playlistwordcloudgenerator.playlist;

import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public interface PlaylistRetriever<T, V> {
  T getPlaylist(V identifier) throws IOException, InterruptedException;
}
