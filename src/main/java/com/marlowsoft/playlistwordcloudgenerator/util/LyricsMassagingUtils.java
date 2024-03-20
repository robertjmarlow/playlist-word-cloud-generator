package com.marlowsoft.playlistwordcloudgenerator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.marlowsoft.playlistwordcloudgenerator.wordcloud.obj.BoringWords;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public final class LyricsMassagingUtils {
  private LyricsMassagingUtils() {}

  public static List<String> removeSectionAnnotations(final List<String> lyrics) {
    final Pattern sectionAnnotationPattern = Pattern.compile("\\[.*?\\]");

    final ImmutableList.Builder<String> updatedLyricsBuilder =
        ImmutableList.builderWithExpectedSize(lyrics.size());

    for (final String songLyrics : lyrics) {
      updatedLyricsBuilder.add(sectionAnnotationPattern.matcher(songLyrics).replaceAll(""));
    }

    return updatedLyricsBuilder.build();
  }

  public static List<String> removeBoringWords(
      final List<String> lyrics, final ObjectMapper objectMapper) throws IOException {
    final BoringWords boringWords =
        objectMapper.readValue(Resources.getResource("boring-words.json"), BoringWords.class);

    final Pattern boringWordsPattern =
        Pattern.compile(
            String.format("\\b(%s)\\b", Joiner.on('|').join(boringWords.getBoringWords())),
            Pattern.CASE_INSENSITIVE);

    final ImmutableList.Builder<String> updatedLyricsBuilder =
        ImmutableList.builderWithExpectedSize(lyrics.size());

    for (final String songLyrics : lyrics) {
      updatedLyricsBuilder.add(boringWordsPattern.matcher(songLyrics).replaceAll(""));
    }

    return updatedLyricsBuilder.build();
  }
}
