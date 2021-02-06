package org.bff.javampd.art;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MPDArtwork {
    private String name;
    private String path;
    private byte[] bytes;

    public MPDArtwork(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
