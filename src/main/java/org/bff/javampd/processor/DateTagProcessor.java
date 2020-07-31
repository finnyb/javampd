package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;

public class DateTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor, AlbumTagResponseProcessor {

  public DateTagProcessor() {
    super("Date:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setYear(line.substring(getPrefix().length()).trim());
    }
  }

  @Override
  public void processTag(MPDAlbum album, String line) {
    if (startsWith(line)) {
      album.setDate(line.substring(getPrefix().length()).trim());
    }
  }
}
