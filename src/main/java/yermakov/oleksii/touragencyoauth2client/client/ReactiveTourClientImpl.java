package yermakov.oleksii.touragencyoauth2client.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yermakov.oleksii.touragencyoauth2client.model.TourDto;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class ReactiveTourClientImpl implements ReactiveTourClient {
    public static final String TOUR_API_PATH = "/tourCrud";
    public static final String TOUR_ID_PATH =  TOUR_API_PATH + "/{tourId}";

    private final WebClient webClient;

    @Override
    public Mono<URI> postTour(TourDto tourDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOUR_API_PATH).build())
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve().toBodilessEntity()
                .flatMap(voidResponseEntity -> Mono.just(URI.create(voidResponseEntity
                        .getHeaders().get("Location").get(0))));
    }

    @Override
    public Mono<Void> deleteTourById(UUID tourId) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<Void> putTourById(UUID tourId, TourDto tourDto) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<Void> patchTourById(UUID tourId, TourDto tourDto) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder.path(TOUR_ID_PATH).build(tourId))
                .body(Mono.just(tourDto), TourDto.class)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
