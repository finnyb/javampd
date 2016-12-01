package org.bff.javampd.server;

import org.bff.javampd.MPDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * I handle loading the properties from the properties file
 *
 * @author bill
 */
public abstract class MPDProperties implements PropertyLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDProperties.class);

    private Properties prop;

    private static final String PROP_FILE = "/mpd.properties";

    protected MPDProperties() {
        loadValues(PROP_FILE);
    }

    @Override
    public String getPropertyString(String property) {
        return prop.getProperty(property);
    }

    protected void loadValues(String propertiesResourceLocation) {
        try (InputStream is = MPDProperties.class.getResourceAsStream(propertiesResourceLocation)) {
           loadProperties(is);
        } catch (NullPointerException | IOException e) {
            LOGGER.error("Could not load properties values", e);
            throw new MPDException("Could not load mpd properties", e);
        }
    }

    protected void loadProperties(InputStream inputStream) throws IOException {
        prop = new Properties();
        prop.load(inputStream);
    }
}
