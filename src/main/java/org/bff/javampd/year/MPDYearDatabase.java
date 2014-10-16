package org.bff.javampd.year;

import com.google.inject.Inject;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the {@link org.bff.javampd.server.MPD#getArtistDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDYearDatabase implements YearDatabase {

    private TagLister tagLister;

    @Inject
    public MPDYearDatabase(TagLister tagLister) {
        this.tagLister = tagLister;
    }

    @Override
    public Collection<String> listAllYears() throws MPDDatabaseException {
        List<String> retList = new ArrayList<>();
        for (String str : tagLister.list(TagLister.ListType.DATE)) {
            if (str.contains("-")) {
                str = str.split("-")[0];
            }

            if (!retList.contains(str)) {
                retList.add(str);
            }
        }
        Collections.sort(retList);
        return retList;
    }
}
