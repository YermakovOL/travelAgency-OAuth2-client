package yermakov.oleksii.touragencyoauth2client.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yermakov.oleksii.touragencyoauth2client.model.TourDto;

import java.net.URI;
import java.util.UUID;

public interface ReactiveTourClient {

    Flux<TourDto> getTours(Integer page, Integer size);

    Mono<URI> postTour(TourDto tourDto);

    Mono<Void> deleteTourById(UUID tourId);

    Mono<TourDto> getTourById(UUID tourId);

    Mono<Void> putTourById(UUID tourId, TourDto tourDto);

    Mono<Void> patchTourById(UUID tourId, TourDto tourDto);
}
