package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.server.MPDErrorEvent;
import org.bff.javampd.server.MPDErrorListener;
import org.bff.javampd.server.Status;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDErrorMonitor implements ErrorMonitor {
    private String error;
    private List<MPDErrorListener> errorListeners;

    public MPDErrorMonitor() {
        this.errorListeners = new ArrayList<>();
    }

    @Override
    public synchronized void addMPDErrorListener(MPDErrorListener el) {
        errorListeners.add(el);
    }

    @Override
    public synchronized void removeMPDErrorListener(MPDErrorListener el) {
        errorListeners.remove(el);
    }

    /**
     * Sends the appropriate {@link MPDErrorListener} to all registered
     * {@link MPDErrorListener}s.
     *
     * @param message the event message
     */
    protected void fireMPDErrorEvent(String message) {
        MPDErrorEvent ee = new MPDErrorEvent(this, message);

        for (MPDErrorListener el : errorListeners) {
            el.errorEventReceived(ee);
        }
    }

    @Override
    public void processResponseStatus(String line) {
        Status status = Status.lookupStatus(line);
        if (status == Status.ERROR) {
            error = line.substring(Status.ERROR.getStatusPrefix().length()).trim();
        }
    }

    @Override
    public void checkStatus() {
        if (error != null) {
            fireMPDErrorEvent(error);
            error = null;
        }
    }
}
