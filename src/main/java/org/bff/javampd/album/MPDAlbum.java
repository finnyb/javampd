package org.bff.javampd.album;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
@Builder(builderMethodName = "internalBuilder")
@Data
public class MPDAlbum implements Comparable<MPDAlbum> {
  @NonNull private String name;
  private String albumArtist;
  @Builder.Default private List<String> artistNames = new ArrayList<>();
  @Builder.Default private List<String> dates = new ArrayList<>();
  @Builder.Default private List<String> genres = new ArrayList<>();

  public static MPDAlbumBuilder builder(String name) {
    return internalBuilder().name(name);
  }

  public void addArtist(String artist) {
    this.artistNames.add(artist);
  }

  public void addArtists(List<String> artists) {
    this.artistNames.addAll(artists);
  }

  public void addDate(String date) {
    this.dates.add(date);
  }

  public void addDates(List<String> dates) {
    this.dates.addAll(dates);
  }

  public void addGenre(String genre) {
    this.genres.add(genre);
  }

  public void addGenres(List<String> genres) {
    this.genres.addAll(genres);
  }

  @Override
  public int compareTo(MPDAlbum album) {
    return this.getName().compareTo(album.getName());
  }
}
