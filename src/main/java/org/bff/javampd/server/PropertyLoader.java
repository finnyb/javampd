package org.bff.javampd.server;

/**
 * Loads properties from the mpd.properties resource bundle
 *
 * @author bill
 */
public interface PropertyLoader {
    /**
     * Returns the properties value
     *
     * @param property the property string key
     * @return the value from the properties file
     */
    String getPropertyString(String property);
}
