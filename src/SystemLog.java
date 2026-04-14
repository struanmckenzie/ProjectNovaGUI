/**
 * System log to store a log of system things
 * @author Struan McKenzie
 */

import java.util.ArrayList;

public class SystemLog<T> {
    // Fields
    private final ArrayList<T> log;

    public SystemLog() {
        log = new ArrayList<>();
    }

    public void add(T data) { log.add(data); }

    public void display() {
        for (T t : log) {
            System.out.println(t);
        }
    }
}
