package net.leirahjelle.playground;

import java.time.Clock;
import java.util.concurrent.ExecutionException;

public class App
{
    public static void main( String[] args ) {
        Clock clock = Clock.systemUTC();
        long startTime = clock.millis();
        Natas18 natas = new Natas18(4);

        String password = null;
        try {
            password = natas.findPassword();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("%nFinal password: %s", password));
        long finishTime = clock.millis() - startTime;
        System.out.println(String.format("Found password in %sms", finishTime));
    }
}
