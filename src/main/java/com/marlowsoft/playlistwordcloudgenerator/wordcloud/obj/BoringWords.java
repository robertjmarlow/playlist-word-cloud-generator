package com.marlowsoft.playlistwordcloudgenerator.wordcloud.obj;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableBoringWords.class)
public interface BoringWords {
  List<String> getBoringWords();
}
