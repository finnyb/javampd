package org.bff.javampd.processor;

public class TrackTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public TrackTagProcessor() {
        super("Track:");
    }

    @Override
    public TagType getType() {
        return TagType.TRACK;
    }
}
