package org.bff.javampd.file;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a file within the mpd songs directory.
 *
 * @author Bill
 */
@Builder(builderMethodName = "internalBuilder")
@Data
public class MPDFile {
  private boolean directory;
  private String path;
  private LocalDateTime lastModified;

  public static MPDFile.MPDFileBuilder builder(String path) {
    return internalBuilder().path(path);
  }
}
