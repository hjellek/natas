package net.leirahjelle.natas;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import java.time.Clock;
import java.util.concurrent.Callable;

public class NatasTask implements Callable<String> {

    private final HttpClient httpClient;
    private final HttpRequestBase request;
    private final TaskMatcher matcher;

    public NatasTask(HttpClient httpClient, HttpRequestBase request, TaskMatcher matcher) {
        this.httpClient = httpClient;
        this.request = request;
        this.matcher = matcher;
    }

    @Override
    public String call() throws Exception {
//        System.out.println("Starting request: "+request.getURI().toASCIIString());
        Clock clock = Clock.systemUTC();
        long startTime = clock.millis();
        HttpResponse response = httpClient.execute(request);
        long requestTime = clock.millis() - startTime;
//        System.out.println(String.format("Finished request: %s in %sms", request.getURI().toASCIIString(), requestTime));
        return matcher.matches(response, requestTime);
    }
}
