package net.leirahjelle.playground;

abstract public class NatasLevel {
    abstract String findPassword();

    protected String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }
}
