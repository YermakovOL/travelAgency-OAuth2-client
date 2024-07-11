
 Full project consists of two microservices:

- **localhost:8080 - [tourRegistration-service](https://github.com/YermakovOL/tourRegistration-service)**
- **localhost:8081 - travelAgency-OAuth2-client**

# Travel Agency OAuth2 Client

## Overview

This microservice acts as an OAuth2 client, utilizing technologies such as OAuth2, Spring WebFlux, and Thymeleaf. In the terminology of OAuth2, this microservice serves as an "OAuth2 client" that interacts with an "Authorization Server" to obtain an access token, granting us access to "Resource Servers".

## OAuth2-client Configuration

In OAuth2 terminology, this microservice acts as an "OAuth2 client". Its purpose is to communicate with the "Authorization Server" to obtain an access token, which will grant access to protected resources on "Resource Servers".

First, we need to configure the endpoints and settings for Google OAuth in the `application.properties` file located at `src/main/resources/application.properties`:

```
properties
spring.security.oauth2.client.registration.google.client-id=${CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo
spring.security.oauth2.client.provider.google.jwk-set-uri=https://www.googleapis.com/oauth2/v3/certs
spring.security.oauth2.client.provider.google.user-name-attribute=sub
```
These settings are intended to integrate your Spring Boot application with the Google OAuth 2.0 authorization system. The first two lines (client-id and client-secret) identify your application to Google, the next line (scope) specifies what user data you want to access. The remaining lines define the Google URLs used for authorization, token acquisition, and user information retrieval. This allows your application to securely access Google user data after obtaining user consent.

Next, we need to configure WebFluxSecurity to integrate with OAuth2Login. The WebFluxSecurityConfig class (src/main/java/yermakov/oleksii/touragencyoauth2client/config/WebFluxSecurityConfig.java) is responsible for this. Spring Security will handle the entire OAuth2 authorization process, leaving you to configure the WebClient to send the access token in requests. You can achieve this by setting up an ExchangeFilterFunction.
```
public ServerOAuth2AuthorizedClientExchangeFilterFunction OAuth2Filter() {
    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
            new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
    oauth.setDefaultOAuth2AuthorizedClient(true);
    return oauth;
}
```
And add it to our WebCLient bean instance:
```
@Bean
public WebClient webClient() {
    return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .filters(exchangeFilterFunctions -> {
                exchangeFilterFunctions.add(OAuth2Filter()); <-------
                exchangeFilterFunctions.add(errorFilter());
                exchangeFilterFunctions.add(logRequest());
                exchangeFilterFunctions.add(logResponse()); 
            })
            .build();
}

```
## Thymeleaf Integration

The web page for using the CRUD API of our resource server is available at localhost:8081/tourPage.

tourPage.html (`src/main/resources/templates/tourPage.html`)
