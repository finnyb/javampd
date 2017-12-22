package org.bff.javampd.monitor;

import org.bff.javampd.server.ErrorListener;

public interface ErrorMonitor extends StatusMonitor {
    void addErrorListener(ErrorListener el);

    void removeErrorListener(ErrorListener el);
}
