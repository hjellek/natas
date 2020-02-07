package net.leirahjelle.playground;

public class App
{
    public static void main( String[] args ) {
        Natas17 natas17 = new Natas17();

        String password = natas17.findPassword();
        System.out.println(String.format("%nPassword: %s", password));
    }
}
