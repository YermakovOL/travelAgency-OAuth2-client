package yermakov.oleksii.touragencyoauth2client.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class TourDto {
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDate startDate;

    private LocalDate endDate;

}
