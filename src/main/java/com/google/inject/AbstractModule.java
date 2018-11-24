

package com.google.inject;

import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import java.util.Map;

public abstract class AbstractModule extends Module {
    protected final <T> AnnotatedBindingBuilder<T> bind(Class<T> aClass) {
        return new ABB<>(aClass, impls);
    }

    private final class ABB<T> implements AnnotatedBindingBuilder<T> {

        private final Class<T> api;
        private final Map<Class<?>, Class<?>> binds;

        ABB(Class<T> api, Map<Class<?>, Class<?>> binds) {
            this.binds = binds;
            this.api = api;
        }

        @Override
        public ScopedBindingBuilder<T> to(Class<? extends T> impl) {
            binds.put(api, impl);
            return null;
        }

    }

}
