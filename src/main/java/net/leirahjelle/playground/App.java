package net.leirahjelle.playground;

import java.util.concurrent.ExecutionException;

public class App
{
    public static void main( String[] args ) {
        Natas17 natas17 = new Natas17(8);

        String password = null;
        try {
            password = natas17.findPassword();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("%nFinal password: %s", password));
    }
}
