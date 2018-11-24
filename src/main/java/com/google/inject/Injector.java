
package com.google.inject;

public interface Injector {
    public <T> T getInstance(Class<T> type);
}
