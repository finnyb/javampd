package org.bff.javampd.monitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.admin.MPDAdminException;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;

import java.util.*;

@Singleton
public class MPDOutputMonitor implements OutputMonitor {
    private Map<Integer, MPDOutput> outputMap;
    private List<OutputChangeListener> outputListeners;

    private Admin admin;

    @Inject
    public MPDOutputMonitor(Admin admin) {
        this.admin = admin;
        this.outputMap = new HashMap<>();
        this.outputListeners = new ArrayList<>();
    }

    @Override
    public void checkStatus() throws MPDAdminException {
        List<MPDOutput> outputs = new ArrayList<>(admin.getOutputs());
        if (outputs.size() > outputMap.size()) {
            fireOutputChangeEvent(new OutputChangeEvent(this, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED));
            loadOutputs(outputs);
        } else if (outputs.size() < outputMap.size()) {
            fireOutputChangeEvent(new OutputChangeEvent(this, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_DELETED));
            loadOutputs(outputs);
        } else {
            for (MPDOutput out : outputs) {
                MPDOutput output = outputMap.get(out.getId());
                if (output == null) {
                    fireOutputChangeEvent(new OutputChangeEvent(out, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED));
                    loadOutputs(outputs);
                    return;
                } else {
                    if (output.isEnabled() != out.isEnabled()) {
                        fireOutputChangeEvent(new OutputChangeEvent(out, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED));
                        loadOutputs(outputs);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputListeners.add(vcl);
    }

    @Override
    public synchronized void removeOutputChangedListener(OutputChangeListener vcl) {
        outputListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link OutputChangeEvent} to all registered
     * {@link org.bff.javampd.output.OutputChangeListener}s.
     *
     * @param event the event id to send
     */
    protected synchronized void fireOutputChangeEvent(OutputChangeEvent event) {
        for (OutputChangeListener ocl : outputListeners) {
            ocl.outputChanged(event);
        }
    }

    private void loadOutputs(Collection<MPDOutput> outputs) {
        outputMap.clear();
        for (MPDOutput output : outputs) {
            outputMap.put(output.getId(), output);
        }
    }
}
