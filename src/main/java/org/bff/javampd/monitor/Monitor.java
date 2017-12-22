package org.bff.javampd.monitor;

@FunctionalInterface
public interface Monitor {
    /**
     * Check the status
     */
    void checkStatus();
}
