package com.hendersonkleber.backend.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.util.Base64;

public class OpaqueTokenUtil {
    public static String generate() {
        byte[] bytes = KeyGenerators.secureRandom(32).generateKey();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String generateHash(String opaqueToken) {
        return DigestUtils.sha256Hex(opaqueToken);
    }
}
