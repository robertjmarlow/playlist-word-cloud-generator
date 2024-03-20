package com.marlowsoft.playlistwordcloudgenerator.lyrics.genius;

import java.net.URL;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class ConnectionRetrieverImpl implements ConnectionRetriever {
  private static final String USER_AGENT =
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0";

  @Override
  public Connection get(URL url) {
    return Jsoup.connect(url.toString()).userAgent(USER_AGENT);
  }
}
