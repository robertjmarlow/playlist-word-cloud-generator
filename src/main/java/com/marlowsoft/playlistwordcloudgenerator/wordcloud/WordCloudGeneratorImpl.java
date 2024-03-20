package com.marlowsoft.playlistwordcloudgenerator.wordcloud;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import java.awt.*;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WordCloudGeneratorImpl implements WordCloudGenerator {
  @Override
  public void generate(List<String> words, String outputFileName) {
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    final WordCloud wordCloud = new WordCloud(new Dimension(600, 600), CollisionMode.PIXEL_PERFECT);
    wordCloud.setPadding(2);
    wordCloud.setBackground(new CircleBackground(300));
    wordCloud.setColorPalette(
        new ColorPalette(
            new Color(0x344E41),
            new Color(0x3A5A40),
            new Color(0x588157),
            new Color(0xA3B18A),
            new Color(0xDAD7CD)));
    wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
    wordCloud.build(frequencyAnalyzer.load(words));
    wordCloud.writeToFile(outputFileName);
  }
}
