package org.bff.javampd.art;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MPDArtwork)) {
            return false;
        }

        MPDArtwork artwork = (MPDArtwork) o;

        return this.path != null ? this.path.equals(artwork.path) : artwork.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
