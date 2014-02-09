package org.bff.javampd;


import org.bff.javampd.objects.MPDSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author bill
 */
public class MPDSongConverter {
    private static Logger logger = LoggerFactory.getLogger(MPDSongConverter.class);

    private static final String PREFIX_FILE = "file:";
    private static final String PREFIX_ARTIST = "Artist:";
    private static final String PREFIX_ALBUM = "Album:";
    private static final String PREFIX_TRACK = "Track:";
    private static final String PREFIX_TITLE = "Title:";
    private static final String PREFIX_DATE = "Date:";
    private static final String PREFIX_GENRE = "Genre:";
    private static final String PREFIX_COMMENT = "Comment:";
    private static final String PREFIX_TIME = "Time:";
    private static final String PREFIX_POS = "Pos:";
    private static final String PREFIX_ID = "Id:";
    private static final String PREFIX_DISC = "Disc:";

    private MPDSongConverter() {
    }

    /**
     * Converts the response from the MPD server into a {@link org.bff.javampd.objects.MPDSong} object.
     *
     * @param list the response from the MPD server
     * @return a MPDSong object
     */
    public static List<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<MPDSong>();
        Iterator<String> iter = list.iterator();
        String line = null;

        while (iter.hasNext()) {
            if (line == null || (!line.startsWith(PREFIX_FILE))) {
                line = iter.next();
            }

            if (line.startsWith(PREFIX_FILE)) {
                MPDSong song = new MPDSong();
                song.setFile(line.substring(PREFIX_FILE.length()).trim());
                line = iter.next();
                while (!line.startsWith(PREFIX_FILE)) {
                    if (line.startsWith(PREFIX_ALBUM)) {
                        song.setAlbumName(line.substring(PREFIX_ALBUM.length()).trim());
                    } else if (line.startsWith(PREFIX_ARTIST)) {
                        song.setArtistName(line.substring(PREFIX_ARTIST.length()).trim());
                    } else if (line.startsWith(PREFIX_TIME)) {
                        song.setLength(Integer.parseInt(line.substring(PREFIX_TIME.length()).trim()));
                    } else if (line.startsWith(PREFIX_TITLE)) {
                        song.setTitle(line.substring(PREFIX_TITLE.length()).trim());
                    } else if (line.startsWith(PREFIX_DATE)) {
                        song.setYear(line.substring(PREFIX_DATE.length()).trim());
                    } else if (line.startsWith(PREFIX_GENRE)) {
                        song.setGenre(line.substring(PREFIX_GENRE.length()).trim());
                    } else if (line.startsWith(PREFIX_COMMENT)) {
                        song.setComment(line.substring(PREFIX_COMMENT.length()).trim());
                    } else if (line.startsWith(PREFIX_TRACK)) {
                        try {
                            song.setTrack(Integer.parseInt(line.substring(PREFIX_TRACK.length()).trim().split("/")[0]));
                        } catch (NumberFormatException nfe) {
                            logger.error("Unable to format track", nfe);
                            song.setTrack(0);
                        }
                    } else if (line.startsWith(PREFIX_POS)) {
                        song.setPosition(Integer.parseInt(line.substring(PREFIX_POS.length()).trim()));
                    } else if (line.startsWith(PREFIX_ID)) {
                        song.setId(Integer.parseInt(line.substring(PREFIX_ID.length()).trim()));
                    } else if (line.startsWith(PREFIX_DISC)) {
                        song.setDiscNumber(line.substring(PREFIX_DISC.length()).trim());
                    }
                    if (!iter.hasNext()) {
                        break;
                    }
                    line = iter.next();
                }
                songList.add(song);
            }
        }
        return songList;
    }

    public static List<String> getSongNameList(List<String> fileList) {
        List<String> names = new ArrayList<String>();
        for (String s : fileList) {
            if (s.startsWith(PREFIX_FILE)) {
                names.add((s.substring(PREFIX_FILE.length())).trim());
            }
        }

        return names;
    }
}
