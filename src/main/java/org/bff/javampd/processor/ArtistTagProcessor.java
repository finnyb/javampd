package org.bff.javampd.processor;

public class ArtistTagProcessor extends TagResponseProcessor implements
        ResponseProcessor {

    public ArtistTagProcessor() {
        super("Artist:");
    }

    @Override
    public TagType getType() {
        return TagType.ARTIST;
    }
}
