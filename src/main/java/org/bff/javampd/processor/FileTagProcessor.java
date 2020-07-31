package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class FileTagProcessor
  extends TagResponseProcessor
  implements SongTagResponseProcessor {

  public FileTagProcessor() {
    super("file:");
  }

  @Override
  public void processTag(MPDSong song, String line) {
    if (startsWith(line)) {
      song.setFile(line.substring(getPrefix().length()).trim());
    }
  }
}
