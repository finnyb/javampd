package org.bff.javampd.album;

import lombok.*;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
@Builder(builderMethodName = "internalBuilder")
@Data
public class MPDAlbum implements Comparable<MPDAlbum> {
    @NonNull
    private String name;
    private String artistName;
    private String date;
    private String genre;

    public static MPDAlbumBuilder builder(String name) {
        return internalBuilder().name(name);
    }

    @Override
    public int compareTo(MPDAlbum album) {
        return this.getName().compareTo(album.getName());
    }
}
