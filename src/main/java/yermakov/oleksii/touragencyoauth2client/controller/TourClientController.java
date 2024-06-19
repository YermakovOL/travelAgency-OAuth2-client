package yermakov.oleksii.touragencyoauth2client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yermakov.oleksii.touragencyoauth2client.client.ReactiveTourClient;
import yermakov.oleksii.touragencyoauth2client.model.TourDto;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TourClientController implements TourOauth2ClientController{
    public static final String TOUR_CLIENT_PATH = "/tour";
    public static final String TOUR_CLIENT_ID_PATH = TOUR_CLIENT_PATH + "/{tourId}";

    private final ReactiveTourClient tourClient;

    @PostMapping(TOUR_CLIENT_PATH)
    public Mono<ResponseEntity<URI>> postTour(@RequestBody TourDto tourDto) {
        return tourClient.postTour(tourDto)
                .flatMap(uri -> Mono.just(ResponseEntity.created(uri).build()));
    }

    @PutMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<Void>> putTour(@PathVariable UUID tourId, @RequestBody TourDto tourDto) {
        return tourClient.putTourById(tourId, tourDto)
                .thenReturn(ResponseEntity.noContent().build());
    }
    @PatchMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<Void>> patchTour(@PathVariable UUID tourId, @RequestBody TourDto tourDto) {
        return tourClient.patchTourById(tourId, tourDto)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<Void>> deleteTour(@PathVariable UUID tourId) {
        return tourClient.deleteTourById(tourId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
