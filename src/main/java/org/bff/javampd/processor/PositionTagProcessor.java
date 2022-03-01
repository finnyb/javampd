package org.bff.javampd.processor;

public class PositionTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public PositionTagProcessor() {
        super("Pos:");
    }

    @Override
    public TagType getType() {
        return TagType.POSITION;
    }
}
