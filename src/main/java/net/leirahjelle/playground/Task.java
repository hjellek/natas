package net.leirahjelle.playground;

import java.net.URI;

public interface Task {
    void execute(URI uri, TaskMatcher matcher);
}
