package yermakov.oleksii.touragencyoauth2client.controller;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import yermakov.oleksii.touragencyoauth2client.model.TourDto;

import java.net.URI;
import java.util.UUID;

public interface TourOauth2ClientController {
    Mono<ResponseEntity<URI>> postTour(TourDto tourDto);
    Mono<ResponseEntity<Void>> putTour(UUID tourId, TourDto tourDto);
    Mono<ResponseEntity<Void>> patchTour(UUID tourId, TourDto tourDto);
    Mono<ResponseEntity<Void>> deleteTour(UUID tourId);
}
