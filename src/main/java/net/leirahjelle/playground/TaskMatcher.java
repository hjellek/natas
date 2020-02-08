package net.leirahjelle.playground;

import org.apache.http.HttpResponse;

public interface TaskMatcher {
    String matches(HttpResponse response, long requestTime);
}
