package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;

public interface Monitor {
    /**
     * Check the status
     *
     * @throws MPDException if there are issues that occur during the check
     */
    void checkStatus();
}
