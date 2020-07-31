package org.bff.javampd.year;

import org.bff.javampd.BaseTest;
import org.bff.javampd.integrationdata.TestYears;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MPDDateDatabaseIT extends BaseTest {
    private DateDatabase dateDatabase;

    @BeforeEach
    public void setUp() {
        this.dateDatabase = getMpd().getMusicDatabase().getDateDatabase();
    }

    @Test
    public void testListAllYears() {
        List<String> resultYears = new ArrayList<String>(dateDatabase.listAllDates());

        List<String> foundYears = new ArrayList<String>(TestYears.getYears());

        assertEquals(resultYears.size(), foundYears.size());

        for (String year : resultYears) {
            boolean exists = false;

            for (String y : foundYears) {
                if (year.equals(y)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Year " + year + " does not exist in list.");
            }
        }
    }
}