# Keycloak

Borrowed from: https://github.com/rwinch/spring-flights/tree/security/keycloak

Admin console credentials: admin/password

Imported user/password:

* brian/password
* rossen/password
* rob/password

## Generate JWT Token

Easily exercise auth code flow via https://oidcdebugger.com

You will need to log into the admin console at http://localhost:8080/ to get the spring-security client secret value.

You can get the auth and token urls from the local keycloak instance
at http://localhost:8080/auth/realms/demo/.well-known/openid-configuration

Use client spring-security and its secret as discovered from the admin console.

```shell
$ curl -H "content-type:application/x-www-form-urlencoded" \
  -d 'grant_type=authorization_code&code=$AUTH_CODE&client_id=spring-security&client_secret=$CLIENT_SECRET&redirect_uri=https%3A%2F%2Foidcdebugger.com%2Fdebug' \
  http://localhost:8080/auth/realms/demo/protocol/openid-connect/token
```

## Run Docker Image

```shell
$ docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=password -e KEYCLOAK_IMPORT=/tmp/demo-realm.json -v "$(pwd -P)/demo-realm.json:/tmp/demo-realm.json" jboss/keycloak:7.0.0
```