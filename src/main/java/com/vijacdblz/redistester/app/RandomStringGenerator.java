package com.vijacdblz.redistester.app;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomStringGenerator {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digit = "1234567890";
    public static final String alphanum = upper + lower + digit;
    public static final String partId = "0123";

    public final Random random;
    private final char[] symbols;
    private final char[] buf;

    public RandomStringGenerator(int length, Random random, String symbols) {
        if (length < 1)
            throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public RandomStringGenerator(int length) {
        this(length, new SecureRandom(), alphanum);
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; idx++) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }

        char randpartId = partId.toCharArray()[random.nextInt(partId.length())];

        LocalDate localDate = LocalDate.now();

        int randomHour = random.nextInt(36);
        int randomMins = random.nextInt(59);

        LocalDate randomDate = localDate.minus(randomHour, ChronoUnit.HOURS);
        randomDate = randomDate.minus(randomMins, ChronoUnit.MINUTES);

        return randpartId + new String(buf) + ":" + randomDate;
    }

}