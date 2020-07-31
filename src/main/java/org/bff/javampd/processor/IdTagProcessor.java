package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class IdTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor {

  public IdTagProcessor() {
    super("Id:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setId(Integer.parseInt(line.substring(getPrefix().length()).trim()));
    }
  }
}
