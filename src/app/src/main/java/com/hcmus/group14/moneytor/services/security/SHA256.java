package com.hcmus.group14.moneytor.services.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String hexadecimalDigest(String message) throws NoSuchAlgorithmException {
        //This returns the hexadecimal digest of the message through SHA-256.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        digest.update(messageBytes);

        byte[] byteDigest = digest.digest();
        StringBuilder hexDigest = new StringBuilder();
        for (int i = 0; i < byteDigest.length; i++)
            hexDigest.append(String.format("%02x", byteDigest[i]));
        return hexDigest.toString();
    }
}
