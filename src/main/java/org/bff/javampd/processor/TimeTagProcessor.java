package org.bff.javampd.processor;

public class TimeTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public TimeTagProcessor() {
        super("Time:");
    }

    @Override
    public TagType getType() {
        return TagType.TIME;
    }
}
