package com.marlowsoft.playlistwordcloudgenerator.playlist.spotify;

import com.marlowsoft.playlistwordcloudgenerator.playlist.PlaylistRetriever;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.ImmutablePlaylist;
import com.marlowsoft.playlistwordcloudgenerator.playlist.spotify.obj.Playlist;
import org.springframework.stereotype.Component;

@Component
public class SpotifyPlaylistRetriever implements PlaylistRetriever<Playlist, String> {
  private static final String ENDPOINT_FORMAT = "https://api.spotify.com/v1/playlists/%s";

  @Override
  public Playlist getPlaylist(String identifier) {
    return ImmutablePlaylist.builder().name("lol hardcoded").build();
  }
}
