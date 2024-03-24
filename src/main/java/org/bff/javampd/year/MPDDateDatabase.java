package org.bff.javampd.year;

import com.google.inject.Inject;
import java.util.Collection;
import org.bff.javampd.database.TagLister;

/**
 * MPDDateDatabase represents a date database controller to a {@link org.bff.javampd.server.MPD}. To
 * obtain an instance of the class you must use the {@link
 * org.bff.javampd.database.MusicDatabase#getDateDatabase} method from the {@link
 * org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDDateDatabase implements DateDatabase {

  private final TagLister tagLister;

  @Inject
  public MPDDateDatabase(TagLister tagLister) {
    this.tagLister = tagLister;
  }

  @Override
  public Collection<String> listAllDates() {

    return tagLister.list(TagLister.ListType.DATE).stream()
        .map(s -> s.substring(s.split(":")[0].length() + 1).trim())
        .distinct()
        .sorted()
        .toList();
  }
}
