package org.bff.javampd.server;

import org.bff.javampd.MPDException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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

    @Test(expected = MPDException.class)
    public void testBadPropertiesLoad() {
        new TestBadPropertiesLoad();
    }

    @Test(expected = MPDException.class)
    public void testNullPropertiesLoad() {
        new TestNullPropertiesLoad();
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
    }

    private class TestBadPropertiesLoad extends MPDProperties {
        @Override
        protected void loadProperties(InputStream inputStream) throws IOException {
            throw new IOException();
        }
    }

    private class TestNullPropertiesLoad extends MPDProperties {
        @Override
        protected void loadProperties(InputStream inputStream) throws IOException {
            throw new NullPointerException();
        }
    }
}