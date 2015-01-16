package org.bff.javampd.year;

import com.google.inject.Inject;
import org.bff.javampd.database.TagLister;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * MPDDateDatabase represents a date database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MPDDatabaseManager#getDateDatabase} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDDateDatabase implements DateDatabase {

    private TagLister tagLister;

    @Inject
    public MPDDateDatabase(TagLister tagLister) {
        this.tagLister = tagLister;
    }

    @Override
    public Collection<String> listAllDates() {

        return tagLister.list(TagLister.ListType.DATE)
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
