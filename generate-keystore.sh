#!/bin/bash

# Generate a keystore for Spring Boot application
echo "Generating keystore for the GPTCheck application..."

# Check if keytool is available
if ! command -v keytool &> /dev/null; then
    echo "Error: keytool is not found. Please make sure Java is installed and in your PATH."
    exit 1
fi

# Destination directory
KEY_DIR="src/main/resources"
mkdir -p $KEY_DIR

# Generate the keystore
keytool -genkeypair \
  -alias gptcheck \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore $KEY_DIR/gptcheck.p12 \
  -validity 3650 \
  -dname "CN=GPTCheck, OU=Development, O=YourOrganization, L=YourCity, ST=YourState, C=US"

# Check if keystore was created successfully
if [ $? -eq 0 ]; then
    echo "Keystore created successfully at $KEY_DIR/gptcheck.p12"
    echo "Make sure to store your keystore password securely!"
    echo "You can generate an encrypted password using CustomPropertyEncryptor utility"
else
    echo "Failed to create keystore."
fi