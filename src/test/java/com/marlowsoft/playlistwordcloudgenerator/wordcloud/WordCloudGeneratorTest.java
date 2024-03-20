package com.marlowsoft.playlistwordcloudgenerator.wordcloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import com.marlowsoft.playlistwordcloudgenerator.util.LyricsMassagingUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("This is mostly here for me to run ad-hoc")
public class WordCloudGeneratorTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  private WordCloudGenerator wordCloudGenerator;

  @BeforeEach
  void beforeEach() {
    wordCloudGenerator = new WordCloudGeneratorImpl();
  }

  @Test
  void makeAWordCloudWithNoWordsRemoved() throws IOException {
    final List<String> lyrics =
        Resources.readLines(Resources.getResource("wordcloud/lyrics.txt"), StandardCharsets.UTF_8);
    wordCloudGenerator.generate(lyrics, "build/test-wordcloud-no-removed-words.png");
  }

  @Test
  void makeAWordCloudWithSectionAnnotationsRemoved() throws IOException {
    final List<String> lyrics =
        LyricsMassagingUtils.removeSectionAnnotations(
            Resources.readLines(
                Resources.getResource("wordcloud/lyrics.txt"), StandardCharsets.UTF_8));
    wordCloudGenerator.generate(lyrics, "build/test-wordcloud-section-annotations-removed.png");
  }

  @Test
  void makeAWordCloudWithBoringWordsRemoved() throws IOException {
    final List<String> lyrics =
        LyricsMassagingUtils.removeBoringWords(
            Resources.readLines(
                Resources.getResource("wordcloud/lyrics.txt"), StandardCharsets.UTF_8),
            OBJECT_MAPPER);
    wordCloudGenerator.generate(lyrics, "build/test-wordcloud-boring-words-removed.png");
  }

  @Test
  void makeAWordCloudWithSectionAnnotationsAndBoringWordsRemoved() throws IOException {
    final List<String> lyrics =
        LyricsMassagingUtils.removeBoringWords(
            LyricsMassagingUtils.removeSectionAnnotations(
                Resources.readLines(
                    Resources.getResource("wordcloud/lyrics.txt"), StandardCharsets.UTF_8)),
            OBJECT_MAPPER);
    wordCloudGenerator.generate(
        lyrics, "build/test-wordcloud-section-annotations-and-boring-words-removed.png");
  }
}
