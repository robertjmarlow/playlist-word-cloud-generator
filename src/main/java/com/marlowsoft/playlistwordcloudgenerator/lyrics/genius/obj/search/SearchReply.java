package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableSearchReply.class)
public interface SearchReply {
  Map<SearchRequest.SearchRequestItem, GeniusSearchReply> getSearchResults();
}
