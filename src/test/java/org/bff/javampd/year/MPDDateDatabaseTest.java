package org.bff.javampd.year;

import org.bff.javampd.database.TagLister;
import org.bff.javampd.processor.DateTagProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDDateDatabaseTest {
    private static String DATE_PREFIX = new DateTagProcessor().getPrefix();

    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDDateDatabase yearDatabase;

    @Test
    public void testListAllYears() throws Exception {
        String year1 = "1990";
        String year2 = "1990-mar-24";

        List<String> yearList = new ArrayList<>();
        yearList.add(DATE_PREFIX + year1);
        yearList.add(DATE_PREFIX + year2);

        when(tagLister
                .list(TagLister.ListType.DATE))
                .thenReturn(yearList);

        List<String> years = new ArrayList<>(yearDatabase.listAllDates());
        assertEquals(2, years.size());
        assertEquals(year1, years.get(0));
        assertEquals(year2, years.get(1));
    }
}