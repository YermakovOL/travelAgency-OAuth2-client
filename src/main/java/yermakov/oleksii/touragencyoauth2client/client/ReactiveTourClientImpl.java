package yermakov.oleksii.touragencyoauth2client.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yermakov.oleksii.touragencyoauth2client.model.TourDto;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
@Service
public class ReactiveTourClientImpl implements ReactiveTourClient {
    public static final String TOUR_API_PATH = "/tourCrud";
    public static final String TOUR_ID_PATH =  TOUR_API_PATH + "/{tourId}";

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080").build();


    @Override
    public Flux<TourDto> getTours(Integer page, Integer size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(TOUR_API_PATH)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToFlux(TourDto.class);
    }

    @Override
    public Mono<URI> postTour(TourDto tourDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOUR_API_PATH).build())
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve()
                .bodyToMono(URI.class);
    }

    @Override
    public Mono<Void> deleteTourById(UUID tourId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<TourDto> getTourById(UUID tourId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .retrieve()
                .bodyToMono(TourDto.class);
    }

    @Override
    public Mono<Void> putTourById(UUID tourId, TourDto tourDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<Void> patchTourById(UUID tourId, TourDto tourDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
