package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public abstract class SongResponseProcessor {

    private String prefix;

    public SongResponseProcessor(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    protected boolean startsWith(String line) {
        return line.startsWith(getPrefix());
    }

    public abstract void processSong(MPDSong song, String line);
}
