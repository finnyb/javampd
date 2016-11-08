package org.bff.javampd.server;

import org.bff.javampd.MPDException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MPDPropertiesTest {

    @Test
    public void testGetPropertyString() throws Exception {
        TestProperties testProperties = new TestProperties();
        assertEquals("OK", testProperties.getOk());
    }

    @Test(expected = MPDException.class)
    public void testBadProperties() {
        new TestBadProperties();
    }

    private class TestProperties extends MPDProperties {
        public String getOk() {
            return getPropertyString("MPD_CMD_RESPONSE_OK");
        }
    }

    private class TestBadProperties extends MPDProperties {
        @Override
        protected void loadValues(String propertiesResourceLocation) {
            super.loadValues("badLocation");
        }

        public String getOk() {
            return getPropertyString("MPD_CMD_RESPONSE_OK");
        }
    }
}