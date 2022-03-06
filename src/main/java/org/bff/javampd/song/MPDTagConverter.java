package org.bff.javampd.song;

import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MPDTagConverter<T extends MPDSong> {

  protected static final String DELIMITING_PREFIX = SongProcessor.getDelimitingPrefix();
  private static final String TERMINATION = SongProcessor.getTermination();
  private static final String LINE_DELIMITER = ":";

  public List<T> convertResponse(List<String> list) {
    var songList = new ArrayList<T>();
    var iterator = list.iterator();

    String line = null;
    while (iterator.hasNext()) {
      if ((!isLineDelimiter(line))) {
        line = iterator.next();
      }

      if (line != null && isLineDelimiter(line)) {
        var props = new HashMap<String, String>();
        var tags = new HashMap<String, List<String>>();
        line =
            processSong(line.substring(DELIMITING_PREFIX.length()).trim(), iterator, props, tags);
        songList.add(createSong(props, tags));
      }
    }

    return songList;
  }

  protected String processSong(
      String fileName,
      Iterator<String> iterator,
      Map<String, String> props,
      Map<String, List<String>> tags) {
    props.put(SongProcessor.FILE.name(), fileName);

    String line = null;
    if (iterator.hasNext()) {
      line = iterator.next();
    }

    while (!isStreamTerminated(line) && !isLineDelimiter(line)) {
      addTag(line, tags);
      var processor = SongProcessor.lookup(line);
      if (processor != null) {
        var tag = processor.getProcessor().processTag(line);
        props.put(processor.name(), tag);
      } else {
        log.warn("Processor not found - {}", line);
      }

      if (iterator.hasNext()) {
        line = iterator.next();
      } else {
        break;
      }
    }

    return line;
  }

  protected abstract T createSong(Map<String, String> props, Map<String, List<String>> tags);

  protected boolean isLineDelimiter(String line) {
    return line != null && line.toLowerCase().startsWith(DELIMITING_PREFIX.toLowerCase());
  }

  protected int parseInt(String tag) {
    try {
      return Integer.parseInt(tag);
    } catch (NumberFormatException e) {
      log.warn(String.format("Unable to convert tag to Integer: %s", tag));
      return -1;
    }
  }

  private boolean isStreamTerminated(String line) {
    return line == null || TERMINATION.equalsIgnoreCase(line) || isLineDelimiter(line);
  }

  private void addTag(String line, Map<String, List<String>> tags) {
    var tag = line.split(LINE_DELIMITER);
    if (tag.length > 1) {
      addToTagMap(tag, tags);
      log.debug("added tag to song: {} -> {}", tag[0], tag[1]);
    } else {
      log.debug("Not adding to tag map, line is not a tag {}", line);
    }
  }

  private void addToTagMap(String[] tag, Map<String, List<String>> tags) {
    var l = tags.get(tag[0]);
    if (l == null) {
      l = new ArrayList<>();
    }
    l.add(tag[1].trim());
    tags.put(tag[0], l);
  }
}
