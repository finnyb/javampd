package org.bff.javampd.properties;

import org.bff.javampd.MPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * I handle loading the properties from the properties file
 *
 * @author bill
 * @since: 11/21/13 6:22 PM
 */
public abstract class MPDProperties {
    private Logger logger = LoggerFactory.getLogger(MPDProperties.class);

    private Properties prop;

    private static final String PROPFILE = "/org/bff/javampd/mpd.properties";

    protected MPDProperties() {
        loadValues();
    }

    protected String getPropertyString(String property) {
        return prop.getProperty(property);
    }

    private void loadValues() {
        prop = new Properties();

        InputStream is = MPD.class.getResourceAsStream(PROPFILE);

        try {
            prop.load(is);
        } catch (Exception e) {
            logger.error("Could not load properties values", e);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                logger.error("Could not close properties file stream", ex);
            }
        }
    }
}
