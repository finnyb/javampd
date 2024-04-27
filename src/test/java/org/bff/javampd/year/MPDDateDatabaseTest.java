package org.bff.javampd.year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.processor.DateTagProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDDateDatabaseTest {
  private static final String DATE_PREFIX = new DateTagProcessor().getPrefix();

  @Mock private TagLister tagLister;

  @InjectMocks private MPDDateDatabase yearDatabase;

  @Test
  void testListAllYears() {
    String year1 = "1990";
    String year2 = "1990-mar-24";

    List<String> yearList = new ArrayList<>();
    yearList.add(DATE_PREFIX + year1);
    yearList.add(DATE_PREFIX + year2);

    when(tagLister.list(TagLister.ListType.DATE)).thenReturn(yearList);

    List<String> years = new ArrayList<>(yearDatabase.listAllDates());
    assertEquals(2, years.size());
    assertEquals(year1, years.getFirst());
    assertEquals(year2, years.get(1));
  }
}
