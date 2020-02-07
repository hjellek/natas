package net.leirahjelle.playground;

import org.apache.http.HttpResponse;

public interface TaskMatcher {
    boolean matches(HttpResponse response);
}
