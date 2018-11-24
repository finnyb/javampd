
package com.google.inject.binder;

public interface AnnotatedBindingBuilder<T> {
    public ScopedBindingBuilder<T> to(Class<? extends T> impl);
}
