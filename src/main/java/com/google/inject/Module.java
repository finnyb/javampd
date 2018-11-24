
package com.google.inject;

import java.util.HashMap;
import java.util.Map;


public abstract class Module {
    final Map<Class<?>,Class<?>> impls = new HashMap<>();

    protected abstract void configure();
}
