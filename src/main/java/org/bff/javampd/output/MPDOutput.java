package org.bff.javampd.output;

import lombok.Builder;
import lombok.Data;

/**
 * Represent a MPD output.
 *
 * @author Bill
 */
@Builder(builderMethodName = "internalBuilder")
@Data
public class MPDOutput {
  private int id;
  private String name;
  private boolean enabled;

  public static MPDOutput.MPDOutputBuilder builder(int id) {
    return internalBuilder().id(id);
  }
}
