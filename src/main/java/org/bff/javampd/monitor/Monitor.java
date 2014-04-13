package org.bff.javampd.monitor;

import org.bff.javampd.exception.MPDException;

public interface Monitor {
    /**
     * The number of seconds to delay before performing the check status.  If your
     * #checkStatus is expensive this should be a larger number.
     *
     * @return the number of seconds to delay
     */
    int getDelay();

    /**
     * Check the status
     *
     * @throws MPDException if there are issues that occur during the check
     */
    void checkStatus() throws MPDException;
}
