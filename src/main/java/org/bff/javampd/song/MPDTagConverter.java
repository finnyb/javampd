package org.bff.javampd.song;

import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.playlist.MPDPlaylistSong;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class MPDTagConverter<T extends MPDSong> {

    protected static final String DELIMITING_PREFIX = SongProcessor.getDelimitingPrefix();
    private static final String TERMINATION = SongProcessor.getTermination();

    public List<T> convertResponse(List<String> list) {
        var songList = new ArrayList<T>();
        var iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if ((!isLineDelimiter(line))) {
                line = iterator.next();
            }

            if (line != null && isLineDelimiter(line)) {
                var props = new HashMap<String, String>();
                line = processSong(
                        line.substring(DELIMITING_PREFIX.length()).trim(),
                        iterator,
                        props);
                songList.add(createSong(props));
            }
        }

        return songList;
    }

    protected String processSong(String fileName,
                               Iterator<String> iterator,
                               Map<String, String> props) {
        props.put(SongProcessor.FILE.name(), fileName);

        String line = null;
        if (iterator.hasNext()) {
            line = iterator.next();
        }

        while (!isStreamTerminated(line) && !isLineDelimiter(line)) {
            var processor = SongProcessor.lookup(line);
            if (processor != null) {
                var tag = processor.getProcessor().processTag(line);
                props.put(processor.name(), tag);
            } else {
                log.warn("Processor not found - {}", line);
            }

            if (iterator.hasNext()) {
                line = iterator.next();
            } else {
                break;
            }
        }

        return line;
    }

    protected abstract T createSong(Map<String, String> props);

    protected boolean isLineDelimiter(String line) {
        return line != null && line.toLowerCase().startsWith(DELIMITING_PREFIX.toLowerCase());
    }

    protected int parseInt(String tag) {
        try {
            return Integer.parseInt(tag);
        } catch (NumberFormatException e) {
            log.warn(String.format("Unable to convert tag to Integer: %s", tag));
            return -1;
        }
    }

    private boolean isStreamTerminated(String line) {
        return line == null ||
                TERMINATION.equalsIgnoreCase(line) ||
                isLineDelimiter(line);
    }
}
