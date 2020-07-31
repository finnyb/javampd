package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;

public class GenreTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor, AlbumTagResponseProcessor {

  public GenreTagProcessor() {
    super("Genre:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setGenre(line.substring(getPrefix().length()).trim());
    }
  }

  @Override
  public void processTag(MPDAlbum album, String line) {
    if (startsWith(line)) {
      album.setGenre(line.substring(getPrefix().length()).trim());
    }
  }
}
