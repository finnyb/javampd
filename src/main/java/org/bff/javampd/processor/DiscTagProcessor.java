package org.bff.javampd.processor;

public class DiscTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public DiscTagProcessor() {
        super("Disc:");
    }

    @Override
    public TagType getType() {
        return TagType.DISC;
    }
}
