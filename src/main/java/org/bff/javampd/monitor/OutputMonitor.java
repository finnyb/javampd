package org.bff.javampd.monitor;

import org.bff.javampd.output.OutputChangeListener;

public interface OutputMonitor extends Monitor {

  /**
   * Adds a {@link org.bff.javampd.output.OutputChangeListener} to this object to receive {@link
   * org.bff.javampd.output.OutputChangeEvent}s.
   *
   * @param vcl the OutputChangeListener to add
   */
  void addOutputChangeListener(OutputChangeListener vcl);

  /**
   * Removes a {@link org.bff.javampd.output.OutputChangeListener} from this object.
   *
   * @param vcl the OutputChangeListener to remove
   */
  void removeOutputChangeListener(OutputChangeListener vcl);
}
