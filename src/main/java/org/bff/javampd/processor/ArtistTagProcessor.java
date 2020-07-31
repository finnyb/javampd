package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;

public class ArtistTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor, AlbumTagResponseProcessor {

  public ArtistTagProcessor() {
    super("Artist:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setArtistName(line.substring(getPrefix().length()).trim());
    }
  }

  @Override
  public void processTag(MPDAlbum album, String line) {
    if (startsWith(line)) {
      album.setArtistName(line.substring(getPrefix().length()).trim());
    }
  }
}
