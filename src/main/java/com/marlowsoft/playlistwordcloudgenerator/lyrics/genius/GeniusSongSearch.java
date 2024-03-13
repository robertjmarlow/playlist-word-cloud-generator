package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchReply;
import com.marlowsoft.playlistwordcloudgenerator.lyrics.genius.obj.search.SearchRequest;
import java.io.IOException;

public interface GeniusSongSearch {
  SearchReply search(SearchRequest searchRequest) throws IOException, InterruptedException;
}
