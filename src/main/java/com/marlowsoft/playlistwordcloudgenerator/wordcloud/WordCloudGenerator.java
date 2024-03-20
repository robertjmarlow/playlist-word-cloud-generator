package com.marlowsoft.playlistwordcloudgenerator.wordcloud;

import java.util.List;

public interface WordCloudGenerator {
  void generate(List<String> words, String outputFileName);
}
