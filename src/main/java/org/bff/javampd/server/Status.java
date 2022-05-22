package org.bff.javampd.server;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/** Enumeration of the available information from the MPD server status. */
@Slf4j
public enum Status {
  /** The current volume (0-100) */
  VOLUME("volume:"),
  /** is the song repeating (0 or 1) */
  REPEAT("repeat:"),
  /** is the song playing in random order (0 or 1) */
  RANDOM("random:"),
  /** the playlist version number (31-bit unsigned integer) */
  PLAYLIST("playlist:"),
  /** the length of the playlist */
  PLAYLISTLENGTH("playlistlength:"),
  /** the current state (play, stop, or pause) */
  STATE("state:"),
  /** playlist song number of the current song stopped on or playing */
  CURRENTSONG("song:"),
  /** playlist song id of the current song stopped on or playing */
  CURRENTSONGID("songid:"),
  /** playlist next song number of the next song to be played */
  NEXT_SONG("nextsong:"),
  /** playlist song id of the next song to be played */
  NEXT_SONG_ID("nextsongid:"),
  /** duration of the current song in seconds */
  DURATION("duration:"),
  /** total time elapsed within the current song in seconds, but with higher resolution */
  ELAPSED("elapsed:"),
  /** the time of the current playing/paused song */
  TIME("time:"),
  /** instantaneous bitrate in kbps */
  BITRATE("bitrate:"),
  /** crossfade in seconds */
  XFADE("xfade:"),
  /** the cuurent samplerate, bits, and channels */
  AUDIO("audio:"),
  /** job id */
  UPDATINGDB("updating_db:"),
  /** if there is an error, returns message here */
  ERROR("error:"),
  /** if 'consume' mode is enabled */
  CONSUME("consume:"),
  /** if 'single' mode is enabled */
  SINGLE("single:"),
  /** if 'single' mode is enabled */
  MIX_RAMP_DB("mixrampdb:"),
  /** if 'single' mode is enabled */
  MIX_RAMP_DELAY("mixrampdelay:"),
  /** if the status is unknown */
  UNKNOWN("unknown");

  /** the prefix associated with the status */
  private final String prefix;

  private static final Map<String, Status> lookup = new HashMap<>();

  static {
    for (Status s : Status.values()) {
      lookup.put(s.prefix, s);
    }
  }

  /**
   * Enum constructor
   *
   * @param prefix the prefix of the line in the response
   */
  Status(String prefix) {
    this.prefix = prefix;
  }

  /**
   * Returns the <CODE>String</CODE> prefix of the response.
   *
   * @return the prefix of the response
   */
  public String getStatusPrefix() {
    return prefix;
  }

  /**
   * Returns the {@link Status} the status line starts with. If no status is found {@link #UNKNOWN}
   * is returned
   *
   * @param line the line to process
   * @return the {@link Status} the lines starts with. <code>null</code> if there isn't a match
   */
  public static Status lookup(String line) {
    var status = lookup.get(line.substring(0, line.indexOf(":") + 1));
    if (status != null) {
      return status;
    }

    log.warn("Unknown status {} returned", line);
    return UNKNOWN;
  }
}
