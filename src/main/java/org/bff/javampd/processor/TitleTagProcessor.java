package org.bff.javampd.processor;

public class TitleTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public TitleTagProcessor() {
        super("Title:");
    }

    @Override
    public TagType getType() {
        return TagType.TITLE;
    }
}
