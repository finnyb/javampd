package org.bff.javampd.audioinfo;

import lombok.Builder;
import lombok.Data;

/** Represents audio information about MPD */
@Builder
@Data
public class MPDAudioInfo {
  private int sampleRate;
  private int bits;
  private int channels;
}
