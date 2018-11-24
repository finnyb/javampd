package org.bff.javampd.year;

import com.google.inject.Inject;
import org.bff.javampd.database.TagLister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MPDDateDatabase represents a date database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getDateDatabase} method from
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

        List<String> list = new ArrayList<>();
        Set<String> uniqueValues = new HashSet<>();
        for (String s : tagLister.list(TagLister.ListType.DATE)) {
            String trim = s.substring(s.split(":")[0].length() + 1).trim();
            if (uniqueValues.add(trim)) {
                list.add(trim);
            }
        }
        Collections.sort(list);
        return list;
    }
}
