# Playlist Word Cloud Generator

Creates a word cloud from all the lyrics in a specified playlist.

## Building

JDK 21+ is needed.

### Windows

```shell
gradle bootJar
```

### MacOS

```shell
./gradlew bootJar
```

## Running

Several environment variables need to be set to retrieve oauth tokens.

### Spotify Auth

Steps to set this up can be found in [Spotify's documentation](https://developer.spotify.com/documentation/web-api/tutorials/getting-started).

After that's done, set the client id and client secret as environment variables:

- `SPOTIFY_CLIENT_ID`
- `SPOTIFY_CLIENT_SECRET`

### Genius Auth

Steps to set this up can be found in [Genius' documentation](https://docs.genius.com/#/getting-started-h1).

After that's done, set the client access token as an environment variable:

- `GENIUS_CLIENT_ACCESS_TOKEN`

### Command line

The bootable jar will end up at `./build/libs/playlistwordcloudgenerator-0.0.1-SNAPSHOT.jar`. Therefore, run this with:

```shell
java -jar ./build/libs/playlistwordcloudgenerator-0.0.1-SNAPSHOT.jar
```

As an example with an actual playlist id (which is required):

```shell
java -jar ./build/libs/playlistwordcloudgenerator-0.0.1-SNAPSHOT.jar 2O7Bd3hKzlaF8IjhGmMzo4
```

### Output

A word cloud will end up in the `./generated-wordclouds` directory with a filename of: `wordcloud-PlayListName.png`.

#### These end up looking weird

This project leverages the [Kumo Word Cloud Generator](https://github.com/kennycason/kumo). The parameters to it are [over in this method](https://github.com/robertjmarlow/playlist-word-cloud-generator/blob/77920d0e4c1056d295bfa833de447f097713d005/src/main/java/com/marlowsoft/playlistwordcloudgenerator/wordcloud/WordCloudGeneratorImpl.java#L18). I normally mess around with the `frequencyAnalyzer.setWordFrequenciesToReturn` (which sets the maximum amount of words to fit into the word cloud) and/or the `wordCloud.setFontScalar(new SqrtFontScalar(10, 60))` (which sets the min / max word size based on the frequency of words). It normally takes a few tires.
