package org.bff.javampd.processor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public IdTagProcessor() {
        super("Id:");
    }

    @Override
    public TagType getType() {
        return TagType.ID;
    }
}
