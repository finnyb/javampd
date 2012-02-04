package org.bff.javampd;

/**
 * Created by IntelliJ IDEA.
 * User: Bill
 * Date: 2/4/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockUtils {

    public static final String getClassName() {
        String line = "";
        StackTraceElement[] elements = new Throwable().getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (!element.getClassName().startsWith("org.bff.javampd")) {
                if (!elements[i - 1].getClassName().contains("BaseTest")) {
                    line = elements[i - 1].getLineNumber() + "-" + elements[i - 1].getClassName();
                } else {
                    line = elements[i - 2].getLineNumber() + "-" + elements[i - 2].getClassName();
                }
                break;
            }
        }
        return line;
    }
}
