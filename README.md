# Security Service

OAuth2 JWT Token Generation and Validation Service for Local Development

> ⚠️ **Local Development Only** - Test/production use an external OAuth2 service.

## Quick Start

```bash
# 1. Generate RSA keys (one-time setup)
generate-jwt-keys.bat  # Windows or ./generate-jwt-keys.sh (Linux/Mac)

# 2. Build and run
mvn clean package && mvn spring-boot:run

# 3. Test token generation
curl -X POST 'http://localhost:8090/oauth2/rest/token' \
  -H 'X-OAUTH-IDENTITY-DOMAIN-NAME: DEV_JET_WebGateDomain' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Authorization: Basic REVWX09NU19PSURNV2ViR2F0ZUlEOmpnYWs4MjRmSGRLMzlnczhnYQ==' \
  -d 'grant_type=CLIENT_CREDENTIALS&scope=DEV_TokenPOC_RS.sharedcomponents'
```

**Prerequisites**: Java 21, Maven 3.9+, OpenSSL

## Setup

**1. Generate RSA Keys** (creates keys in `~/jwt-keys/` or `%USERPROFILE%\jwt-keys\`)
```bash
generate-jwt-keys.bat     # Windows
./generate-jwt-keys.sh    # Linux/Mac
```

**2. Start Service**
```bash
mvn spring-boot:run       # Service runs on port 8090
```

## Default Test Credentials

| Property      | Value                                                            |
|---------------|------------------------------------------------------------------|
| Client ID     | `DEV_OMS_OIDMWebGateID`                                          |
| Client Secret | `jgak824fHdK39gs8ga`                                             |
| Domain        | `DEV_JET_WebGateDomain`                                          |
| Scope         | `DEV_TokenPOC_RS.sharedcomponents`                               |
| Basic Auth    | `Basic REVWX09NU19PSURNV2ViR2F0ZUlEOmpnYWs4MjRmSGRLMzlnczhnYQ==` |

### Adding New Clients

**Generate BCrypt hash for password:**
```bash
mvn test -Dtest=PasswordHashGeneratorTest -DgenerateHash=true
```

**Add to `application.yml`:**
```yaml
oauth2:
  clients:
    - client-id: YOUR_CLIENT_ID
      client-secret: $2a$10$YOUR_BCRYPT_HASH
      allowed-scopes: [YOUR_SCOPE]
      identity-domain: YOUR_DOMAIN
```


## API Endpoints

### 1. Generate Token - `POST /oauth2/rest/token`

```bash
curl -X POST 'http://localhost:8090/oauth2/rest/token' \
  -H 'X-OAUTH-IDENTITY-DOMAIN-NAME: DEV_JET_WebGateDomain' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Authorization: Basic REVWX09NU19PSURNV2ViR2F0ZUlEOmpnYWs4MjRmSGRLMzlnczhnYQ==' \
  -d 'grant_type=CLIENT_CREDENTIALS&scope=DEV_TokenPOC_RS.sharedcomponents'
```

**Response:**
```json
{
  "access_token": "eyJraWQi...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "DEV_TokenPOC_RS.sharedcomponents"
}
```

### 2. Validate Token - `GET /oauth2/rest/token/info`

```bash
curl 'http://localhost:8090/oauth2/rest/token/info?access_token=<JWT_TOKEN>' \
  -H 'X-OAUTH-IDENTITY-DOMAIN-NAME: DEV_JET_WebGateDomain'
```

**Response:**
```json
{
  "iss": "https://localhost:8090/oauth2",
  "aud": ["https://localhost:8090/oauth2", "DEV_TokenPOC_RS"],
  "exp": 1765885144,
  "sub": "DEV_OMS_OIDMWebGateID",
  "client": "DEV_OMS_OIDMWebGateID",
  "scope": ["DEV_TokenPOC_RS.sharedcomponents"],
  "domain": "DEV_JET_WebGateDomain"
}
```

## Error Codes

- `invalid_client` - Invalid credentials
- `unsupported_grant_type` - Only CLIENT_CREDENTIALS supported
- `invalid_request` - Missing required parameters
- `invalid_token` - Token validation failed

## Microservice Integration

Add `security-utilities` dependency and configure:

```yaml
# application.yml
security:
  filter:
    jwt-enabled: true
    token-validation-url: http://localhost:8090/oauth2/rest/token/info
```

See `monitoring-service` for complete example.

## Troubleshooting

**Missing RSA Keys**: Run `generate-jwt-keys.bat` or `./generate-jwt-keys.sh`

**Port Conflict**: Change port in `application.yml`:
```yaml
server:
  port: 8091
```

**401 Error**: Verify credentials match default test configuration above.

---

**Tech Stack**: Spring Boot 3.5.8 • Spring Security • JJWT 0.12.6 • Java 21

