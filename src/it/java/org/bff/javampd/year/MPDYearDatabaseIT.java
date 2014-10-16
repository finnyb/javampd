package org.bff.javampd.year;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.integrationdata.TestYears;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MPDYearDatabaseIT extends BaseTest {
    private YearDatabase yearDatabase;

    @Before
    public void setUp() throws MPDException, IOException {
        this.yearDatabase = getMpd().getDatabaseManager().getYearDatabase();
    }

    @Test
    public void testListAllYears() throws MPDException {
        List<String> resultYears = new ArrayList<String>(yearDatabase.listAllYears());

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