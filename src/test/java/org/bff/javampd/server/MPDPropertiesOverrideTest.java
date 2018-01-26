package org.bff.javampd.server;

import org.bff.javampd.monitor.MonitorProperties;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertNotEquals;

public class MPDPropertiesOverrideTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDPropertiesOverrideTest.class);

    private static File propertiesFile;
    private MPDProperties properties;

    @BeforeClass
    public static void beforeClass() throws IOException {
        InputStream is = MPDPropertiesOverrideTest.class.getResourceAsStream("/overrides/javampd.properties");
        Properties properties = new Properties();
        properties.load(is);

        String propPath = MPDPropertiesOverrideTest.class.getResource("/")
                .toString()
                .replace("file:", "");

        propertiesFile = new File(propPath + "javampd.properties");
        try {
            propertiesFile.createNewFile();
        } catch (IOException e) {
            LOGGER.error("unable to create file in {}", propPath, e);
            throw e;
        }

        properties.store(new FileWriter(propertiesFile), "");
    }

    @AfterClass
    public static void afterClass() {
        propertiesFile.delete();
    }

    @Before
    public void before() {
        properties = new MonitorProperties();
    }

    @Test
    public void testAllMonitorOverrides() throws Exception {
        Properties originalProperties = new Properties();
        InputStream is = MPDProperties.class.getResourceAsStream("/mpd.properties");
        originalProperties.load(is);
        originalProperties.keySet()
                .stream()
                .filter(key -> ((String) key).startsWith("monitor"))
                .forEach(key -> assertNotEquals(originalProperties.getProperty((String) key), properties.getPropertyString((String) key)));
    }
}