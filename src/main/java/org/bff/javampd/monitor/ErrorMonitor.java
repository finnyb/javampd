package org.bff.javampd.monitor;

import org.bff.javampd.events.MPDErrorListener;

public interface ErrorMonitor extends StatusMonitor {
    void addMPDErrorListener(MPDErrorListener el);

    void removeMPDErrorListener(MPDErrorListener el);
}
