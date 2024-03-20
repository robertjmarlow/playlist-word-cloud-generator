package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import java.net.URL;
import org.jsoup.Connection;

public interface ConnectionRetriever {
  Connection get(URL url);
}
