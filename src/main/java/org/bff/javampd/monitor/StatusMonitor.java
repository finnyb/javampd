package org.bff.javampd.monitor;

public interface StatusMonitor extends Monitor {
    void processResponseStatus(String line);
}
