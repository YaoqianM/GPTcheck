package com.example.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class CustomPropertyEncryptor {
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String SECRET_KEY = System.getProperty("JASYPT_ENCRYPTOR_PASSWORD");

    public static StandardPBEStringEncryptor getEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(SECRET_KEY);
        config.setAlgorithm(ALGORITHM);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }

    public static String encrypt(String val) {
        return getEncryptor().encrypt(val);
    }

    public static String decrypt(String val) {
        return getEncryptor().decrypt(val);
    }
    public static void main(String[] args) {
        String plain = "RJ2+VX1+xDbCH1s8+0BFR++N+xqXcpIp";             // your keystore password
        String cipher = encrypt(plain);
        System.out.println("Encrypted: " + cipher);
    }
}
