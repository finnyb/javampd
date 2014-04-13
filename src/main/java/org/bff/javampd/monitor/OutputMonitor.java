package org.bff.javampd.monitor;

import org.bff.javampd.events.OutputChangeListener;

public interface OutputMonitor extends Monitor {

    /**
     * Adds a {@link org.bff.javampd.events.OutputChangeListener} to this object to receive
     * {@link org.bff.javampd.events.OutputChangeEvent}s.
     *
     * @param vcl the OutputChangeListener to add
     */
    void addOutputChangeListener(OutputChangeListener vcl);

    /**
     * Removes a {@link org.bff.javampd.events.OutputChangeListener} from this object.
     *
     * @param vcl the OutputChangeListener to remove
     */
    void removeOutputChangedListener(OutputChangeListener vcl);
}
