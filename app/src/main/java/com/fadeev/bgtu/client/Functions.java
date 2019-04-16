package com.fadeev.bgtu.client;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Functions {
    public static String generateHash(String input) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toHexString(0xff & anEncodedHash));
        }
        Log.d("Generated hash ", hexString.toString());
        return hexString.toString();

    }
}
