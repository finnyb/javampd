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

    private static final String PROPFILE = "/mpd.properties";

    protected MPDProperties() {
        loadValues(PROPFILE);
    }

    @Override
    public String getPropertyString(String property) {
        return prop.getProperty(property);
    }

    protected void loadValues(String propertiesResourceLocation) {
        prop = new Properties();

        InputStream is = MPDProperties.class.getResourceAsStream(propertiesResourceLocation);

        try {
            prop.load(is);
        } catch (Exception e) {
            LOGGER.error("Could not load properties values", e);
            throw new MPDException("Could not load mpd porporties", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    LOGGER.error("Could not close properties file stream", ex);
                }
            }

        }
    }
}
