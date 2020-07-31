package org.bff.javampd.artist;

import java.util.Iterator;
import org.bff.javampd.MPDItem;

public abstract class MPDConverter {

  protected String processItem(
    MPDItem mpdItem,
    Iterator<String> iterator,
    String delimitingPrefix
  ) {
    String line = null;
    if (iterator.hasNext()) {
      line = iterator.next();
      while (!line.startsWith(delimitingPrefix)) {
        processLine(mpdItem, line);
        if (!iterator.hasNext()) {
          break;
        }
        line = iterator.next();
      }
    }

    return line;
  }

  public abstract void processLine(MPDItem item, String line);
}
