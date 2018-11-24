package com.google.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class GuiceTest {

    public GuiceTest() {
    }

    private static final class BrokenRunnable implements Runnable {

        BrokenRunnable() {
            throw new NumberFormatException("err");
        }

        @Override
        public void run() {
        }

    }

    @Test
    public void testSingletonConstructor() {
        class BrokenModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(Runnable.class).to(BrokenRunnable.class);
            }
        }
        Injector in = Guice.createInjector(new BrokenModule());
        try {
            in.getInstance(Runnable.class);
        } catch (IllegalStateException ex) {
            assertNotNull(ex.getCause());
            final Throwable nfe = ex.getCause().getCause();
            assertNotNull("No next exception for " + ex.getCause(), nfe);
            assertEquals(NumberFormatException.class, nfe.getClass());
            assertEquals("err", nfe.getMessage());
        }
    }

    @Test
    public void constructorIsntPublic() throws Exception {
        Constructor<Guice> c = Guice.class.getDeclaredConstructor();
        assertEquals("Not public", 0, c.getModifiers() & Modifier.PUBLIC);
        assertEquals("Not protected", 0, c.getModifiers() & Modifier.PROTECTED);
        assertNotEquals("Private", 0, c.getModifiers() & Modifier.PRIVATE);
        c.setAccessible(true);
        Guice inst = c.newInstance();
        assertNotNull("Hidden instance created", inst);
    }

    @Test
    public void testSingletonConstructorWithParameters() {
        class BrokenModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(CharSequence.class).to(StringBuilder.class);
                bind(Runnable.class).to(BrokenRunnableWithParameter.class);
            }
        }
        Injector in = Guice.createInjector(new BrokenModule());
        try {
            in.getInstance(Runnable.class);
        } catch (IllegalStateException ex) {
            assertNotNull(ex.getCause());
            final Throwable nfe = ex.getCause().getCause();
            assertNotNull("No next exception for " + ex.getCause(), nfe);
            assertEquals(NumberFormatException.class, nfe.getClass());
            assertEquals("error: java.lang.StringBuilder", nfe.getMessage());
        }
    }

    private static final class BrokenRunnableWithParameter implements Runnable {
        @Inject
        BrokenRunnableWithParameter(CharSequence s) {
            throw new NumberFormatException("error: " + s.getClass().getName());
        }

        @Override
        public void run() {
        }

    }
}
