package com.marlowsoft.playlistwordcloudgenerator.playlist;

import org.springframework.stereotype.Component;

@Component
public interface PlaylistRetriever<T, V> {
  T getPlaylist(V identifier);
}
