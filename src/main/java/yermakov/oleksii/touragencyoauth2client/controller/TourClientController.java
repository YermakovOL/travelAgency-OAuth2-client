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
public class TourClientController {
    public static final String TOUR_CLIENT_PATH = "/tour";
    public static final String TOUR_CLIENT_ID_PATH = TOUR_CLIENT_PATH + "/{tourId}";

    private final ReactiveTourClient tourClient;

    @GetMapping
    public Flux<TourDto> listBeers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "25") Integer size){
        return tourClient.getTours(page, size);
    }

    @PostMapping
    public Mono<ResponseEntity<URI>> createTour(@RequestBody TourDto tourDto) {
        return tourClient.postTour(tourDto)
                .map(uri -> ResponseEntity.created(uri).build());
    }

    @GetMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<TourDto>> getTourById(@PathVariable UUID tourId) {
        return tourClient.getTourById(tourId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Handle not found
    }

    @PutMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<Void>> updateTour(@PathVariable UUID tourId, @RequestBody TourDto tourDto) {
        return tourClient.putTourById(tourId, tourDto)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping(TOUR_CLIENT_ID_PATH)
    public Mono<ResponseEntity<Void>> deleteTour(@PathVariable UUID tourId) {
        return tourClient.deleteTourById(tourId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
