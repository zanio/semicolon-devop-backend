package com.semicolondevop.suite.util.helper;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 12:44 AM
 * @project com.semicolondevop.suite.util.helper in ds-suite
 */
public final class RandomString {
    /* Assign a string that contains the set of characters you allow. */
    private static final String symbols = "ABCDEFGJKLMNPRSTUVWXYZabcdefghijklmnopqrstuvwsyz0123456789";

    private final Random random = new SecureRandom();

    private final char[] buf;

    public RandomString(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public String nextString(String str)
    {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols.charAt(random.nextInt(symbols.length()));
        return str+"-"+new String(buf).toUpperCase();
    }



}



