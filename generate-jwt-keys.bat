@echo off
REM Script to generate RSA key pair for JWT token signing
REM This script creates a private and public key pair in PEM format

SET KEY_DIR=%USERPROFILE%\jwt-keys
SET PRIVATE_KEY=%KEY_DIR%\private_key.pem
SET PUBLIC_KEY=%KEY_DIR%\public_key.pem

echo Creating directory for JWT keys...
if not exist "%KEY_DIR%" mkdir "%KEY_DIR%"

echo Generating RSA private key...
openssl genrsa -out "%PRIVATE_KEY%" 2048

echo Generating RSA public key...
openssl rsa -in "%PRIVATE_KEY%" -pubout -out "%PUBLIC_KEY%"

echo.
echo Keys generated successfully!
echo Private key: %PRIVATE_KEY%
echo Public key:  %PUBLIC_KEY%
echo.
echo Please keep your private key secure and do not commit it to version control.
pause

