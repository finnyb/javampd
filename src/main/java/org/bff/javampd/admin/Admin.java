package org.bff.javampd.admin;

import java.util.Collection;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeListener;

/**
 * Performs {@link org.bff.javampd.server.MPD} administrative tasks
 *
 * @author bill
 */
public interface Admin {
  /**
   * Returns the information about all outputs
   *
   * @return a <code>Collection</code> of {@link org.bff.javampd.output.MPDOutput}
   */
  Collection<MPDOutput> getOutputs();

  /**
   * Disables the passed {@link MPDOutput}
   *
   * @param output the output to disable
   * @return true if the output is disabled
   */
  boolean disableOutput(MPDOutput output);

  /**
   * Enables the passed {@link MPDOutput}
   *
   * @param output the output to enable
   * @return true if the output is enabled
   */
  boolean enableOutput(MPDOutput output);

  /**
   * Adds a {@link MPDChangeListener} to this object to receive {@link MPDChangeEvent}s.
   *
   * @param mcl the MPDChangeListener to add
   */
  void addMPDChangeListener(MPDChangeListener mcl);

  /**
   * Removes a {@link MPDChangeListener} from this object.
   *
   * @param mcl the MPDChangeListener to remove
   */
  void removeMPDChangeListener(MPDChangeListener mcl);

  /** Kills the mpd connection. */
  void killMPD();

  /**
   * Updates the music database: finds new files, removes deleted files, updates modified files.
   *
   * @return a positive number identifying the update job. You can read the current job id in the *
   *     {@link org.bff.javampd.server.Status} response.
   */
  int updateDatabase();

  /**
   * Updates the music database: finds new files, removes deleted files, updates modified files.
   *
   * @param path a particular directory or song/file to update
   * @return a positive number identifying the update job. You can read the current job id in the
   *     {@link org.bff.javampd.server.Status} response.
   */
  int updateDatabase(String path);

  /**
   * Same as {@link #updateDatabase()}, but also rescans unmodified files.
   *
   * @return a positive number identifying the update job. You can read the current job id in the
   *     {@link org.bff.javampd.server.Status} response.
   */
  int rescan();

  /**
   * Adds a {@link OutputChangeListener} to this object to receive {@link
   * org.bff.javampd.playlist.PlaylistChangeEvent}s.
   *
   * @param pcl the PlaylistChangeListener to add
   */
  void addOutputChangeListener(OutputChangeListener pcl);

  /**
   * Removes a {@link OutputChangeListener} from this object.
   *
   * @param pcl the PlaylistChangeListener to remove
   */
  void removeOutputChangeListener(OutputChangeListener pcl);
}
