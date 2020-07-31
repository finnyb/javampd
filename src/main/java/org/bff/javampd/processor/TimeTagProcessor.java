package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class TimeTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor {

  public TimeTagProcessor() {
    super("Time:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setLength(
        Integer.parseInt(line.substring(getPrefix().length()).trim())
      );
    }
  }
}
