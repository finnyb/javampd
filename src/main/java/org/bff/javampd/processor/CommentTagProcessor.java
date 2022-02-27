package org.bff.javampd.processor;

public class CommentTagProcessor extends TagResponseProcessor implements ResponseProcessor {

    public CommentTagProcessor() {
        super("Comment:");
    }

    @Override
    public TagType getType() {
        return TagType.COMMENT;
    }
}
