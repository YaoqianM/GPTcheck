#!/usr/bin/env bash
# Generates PKCS12 keystore for config-service
keytool -genkeypair \
  -alias config-service \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore config/src/main/resources/config-service.p12 \
  -validity 3650 \
  -storepass RJ2+VX1+xDbCH1s8+0BFR++N+xqXcpIp \
  -keypass RJ2+VX1+xDbCH1s8+0BFR++N+xqXcpIp \
  -dname "CN=Config Service, OU=Dev, O=YourOrg, L=City, ST=State, C=US"

echo "Generated config-service.p12 in config/src/main/resources"
