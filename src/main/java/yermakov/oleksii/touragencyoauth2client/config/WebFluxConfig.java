package yermakov.oleksii.touragencyoauth2client.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebFluxConfig {
    public final ReactiveClientRegistrationRepository clientRegistrations;
    public final ServerOAuth2AuthorizedClientRepository authorizedClients;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(OAuth2Filter());
                    exchangeFilterFunctions.add(errorFilter());
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    public ServerOAuth2AuthorizedClientExchangeFilterFunction OAuth2Filter() {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        return oauth;
    }

    private ExchangeFilterFunction errorFilter() {
        return (clientRequest, nextFilter) ->
                nextFilter.exchange(clientRequest)
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is4xxClientError()) {
                                return clientResponse.bodyToMono(String.class)
                                        .flatMap(errorBody -> {
                                            if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorBody));
                                            } else if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                                                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, errorBody));
                                            } else {
                                                return Mono.error(new ResponseStatusException(clientResponse.statusCode(), errorBody));
                                            }
                                        });
                            }
                            return Mono.just(clientResponse);
                        });
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(value -> sb.append(name).append(":").append(value)));
                log.info(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientRequest -> {
            if (log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("Response: \n");
                //append clientRequest method and url
                clientRequest
                        .headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> sb.append(name).append(value)));
                log.info(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }
}
