package com.marlowsoft.playlistwordcloudgenerator.wordcloud;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WordCloudGeneratorImpl implements WordCloudGenerator {
  @Override
  public void generate(List<String> words, String outputFileName) {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    frequencyAnalyzer.setWordFrequenciesToReturn(400);
    final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(words);
    final WordCloud wordCloud =
        new WordCloud(new Dimension(1000, 1000), CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(2);
    wordCloud.setBackground(new CircleBackground(500));
    wordCloud.setColorPalette(
        new ColorPalette(
            new Color(0x344E41),
            new Color(0x3A5A40),
            new Color(0x588157),
            new Color(0xA3B18A),
            new Color(0xDAD7CD)));
    wordCloud.setFontScalar(new SqrtFontScalar(10, 60));
    wordCloud.build(wordFrequencies);
    wordCloud.writeToFile(outputFileName);
  }
}
