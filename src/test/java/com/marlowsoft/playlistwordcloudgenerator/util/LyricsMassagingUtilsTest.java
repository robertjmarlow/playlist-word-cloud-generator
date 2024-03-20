package com.marlowsoft.playlistwordcloudgenerator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlowsoft.playlistwordcloudgenerator.inject.PlaylistWordCloudGeneratorConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class LyricsMassagingUtilsTest {
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new PlaylistWordCloudGeneratorConfig().getObjectMapper();
  }

  @Test
  void testRemoveSectionAnnotations() {
    final List<String> lyrics =
        Arrays.asList(
            "[Chorus] That was my brother Sylvest (What's he got?)",
            "[Break] Ba-dap, ba-dap, ba-dap Ba-da-ba-da-bap, ba-dap Ba-dap, ba-dap, ba-dap Ba-da-ba-da-bap, ba-dap [Verse 3] Joe Murphy fought with Reilly near the Cliffs of Old Dooneen",
            "[Verse 1] I was twenty-four years old When I met the woman I would call my own");

    final List<String> updatedLyrics = LyricsMassagingUtils.removeSectionAnnotations(lyrics);

    assertEquals(lyrics.size(), updatedLyrics.size());

    assertEquals(" That was my brother Sylvest (What's he got?)", updatedLyrics.get(0));
    assertEquals(
        " Ba-dap, ba-dap, ba-dap Ba-da-ba-da-bap, ba-dap Ba-dap, ba-dap, ba-dap Ba-da-ba-da-bap, ba-dap  Joe Murphy fought with Reilly near the Cliffs of Old Dooneen",
        updatedLyrics.get(1));
    assertEquals(
        " I was twenty-four years old When I met the woman I would call my own",
        updatedLyrics.get(2));
  }

  @Test
  void testRemoveBoringWords() throws IOException {
    final List<String> lyrics =
        Arrays.asList(
            "we're shipping out today Now the captain roars when we drop our oars And we're living in a puddle of decay",
            "\"Will you kindly tell to me Who owns that pipe upon the chair where my old pipe should be?\"",
            "A row of forty medals on his chest, big chest Killed fifty badmen in the west");

    final List<String> updatedLyrics =
        LyricsMassagingUtils.removeBoringWords(lyrics, OBJECT_MAPPER);

    assertEquals(lyrics.size(), updatedLyrics.size());

    assertEquals(
        "we're shipping out today Now  captain roars when we drop our oars  we're living in a puddle  decay",
        updatedLyrics.get(0));
    assertEquals(
        "\"Will  kindly tell  me Who owns that pipe upon  chair where my old pipe should be?\"",
        updatedLyrics.get(1));
    assertEquals(
        "A row  forty medals  his chest, big chest Killed fifty badmen in  west",
        updatedLyrics.get(2));
  }
}
