package de.nodelab.dynmc.master;

import com.google.common.io.BaseEncoding;

import java.util.Random;

public class Utils {

    private static Random random;

    static {
        random = new Random();
    }

    public static String randomKey() {
        byte[] randomBytes = new byte[8];
        random.nextBytes(randomBytes);
        return BaseEncoding.base16().lowerCase().encode(randomBytes);
    }

}
