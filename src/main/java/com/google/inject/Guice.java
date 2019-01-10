
package com.google.inject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class Guice {
    private Guice() {
    }

    public static Injector createInjector(Module... sources) {
        Map<Class<?>,Class<?>> pool = new HashMap<>();
        for (Module am : sources) {
            am.configure();
            pool.putAll(am.impls);
        }
        return new InjectorImpl(pool);
    }

    private static final class InjectorImpl implements Injector {

        private final Map<Class<?>, Value> pool;

        InjectorImpl(Map<Class<?>, Class<?>> pool) {
            this.pool = new HashMap<>();
            for (Map.Entry<Class<?>, Class<?>> entry : pool.entrySet()) {
                Class<? extends Object> key = entry.getKey();
                Class<? extends Object> value = entry.getValue();
                this.pool.put(key, new Value(value));
            }
        }

        @Override
        public <T> T getInstance(Class<T> type) {
            Value value = this.pool.get(type);
            if (value == null) {
                Object ret = defaultInstance(type, null);
                return type.cast(ret);
            }
            Object ret = value.instance;
            if (ret == null) {
                Constructor<?>[] used = { null };
                for (Constructor<?> c : value.type.getDeclaredConstructors()) {
                    if (c.getAnnotation(Inject.class) != null) {
                        Object[] args = new Object[c.getParameterTypes().length];
                        for (int i = 0; i < args.length; i++) {
                            args[i] = getInstance(c.getParameterTypes()[i]);
                        }
                        used[0] = c;
                        try {
                            c.setAccessible(true);
                            ret = c.newInstance(args);
                        } catch (Exception ex) {
                            throw new IllegalStateException("Cannot instantiate " + value.type, ex);
                        }
                        break;
                    }
                }
                if (used[0] == null) {
                    ret = defaultInstance(value.type, used);

                }
                if (isSingleton(used)) {
                    value.instance = ret;
                }
            }
            return type.cast(ret);
        }

        private static boolean isSingleton(Constructor<?>[] used) {
            if (used[0].getDeclaringClass().getAnnotation(Singleton.class) != null) {
                return true;
            }
            return false;
        }

        private Object defaultInstance(Class<?> type, Constructor[] used) throws IllegalStateException {
            try {
                Constructor<?> c = type.getDeclaredConstructor();
                c.setAccessible(true);
                Object ret = c.newInstance();
                if (used != null) {
                    used[0] = c;
                }
                return ret;
            } catch (Exception exception) {
                throw new IllegalStateException("Cannot instantiate " + type.getName(), exception);
            }
        }

        private static final class Value {
            final Class<?> type;
            Object instance;

            Value(Class<?> type) {
                this.type = type;
            }
        }
    }
}
