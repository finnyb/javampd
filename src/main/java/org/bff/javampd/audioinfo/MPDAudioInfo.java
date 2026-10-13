package org.bff.javampd.audioinfo;

/** Represents audio information about MPD */
public record MPDAudioInfo(String dsd, int sampleRate, int bits, int channels) {

  public MPDAudioInfo(int sampleRate, int bits, int channels) {
    this(null, sampleRate, bits, channels);
  }

  public MPDAudioInfo(String dsd, int channels) {
    // TODO - set rate based on DSDXXX
    this(dsd, 0, 1, channels);
  }

  @Override
  public String toString() {
    if (dsd == null) {
      return "Sample Rate: " + sampleRate + "; Bits: " + bits + "; Channels: " + channels;
    } else {
      return dsd.toUpperCase() + "; Channels: " + channels;
    }
  }
}
