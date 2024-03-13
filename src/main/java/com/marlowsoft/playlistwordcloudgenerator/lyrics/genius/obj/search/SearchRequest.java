package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search;

import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface SearchRequest {
  Set<SearchRequestItem> getSearchRequestItems();

  @Value.Immutable
  interface SearchRequestItem {
    String getArtist();

    String getTrack();
  }
}
