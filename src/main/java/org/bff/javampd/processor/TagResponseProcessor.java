package org.bff.javampd.processor;

public abstract class TagResponseProcessor {

    private String prefix;

    public TagResponseProcessor(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    protected boolean startsWith(String line) {
        return line.startsWith(getPrefix());
    }

}
