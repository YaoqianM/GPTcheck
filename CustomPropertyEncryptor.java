package com.gptcheck.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class CustomPropertyEncryptor {
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String SECRET_KEY_ENV = "GPTCHECK_ENCRYPTION_KEY";

    public static StandardPBEStringEncryptor getEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(ALGORITHM);

        // Get the encryption key from environment variable
        String secretKey = System.getenv(SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("Encryption key not found in environment variables");
        }

        encryptor.setPassword(secretKey);
        return encryptor;
    }

    public static String encrypt(String propertyValue) {
        return getEncryptor().encrypt(propertyValue);
    }

    public static String decrypt(String encryptedValue) {
        return getEncryptor().decrypt(encryptedValue);
    }

    // Utility method to generate encrypted values for configuration
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CustomPropertyEncryptor <value-to-encrypt>");
            return;
        }

        // For testing purposes only, hardcode a secret key
        System.setProperty(SECRET_KEY_ENV, "test-encryption-key");

        String encrypted = encrypt(args[0]);
        System.out.println("Encrypted value: " + encrypted);
    }
}