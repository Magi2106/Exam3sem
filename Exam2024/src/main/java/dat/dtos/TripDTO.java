package dat.dtos;

import dat.entities.Trip;
import dat.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {
    private Long id;
    private String name;
    private double price;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private String startposition;
    private String description;
    private Category category;
    private GuideDTO guide;

    public TripDTO(Trip trip, boolean includeGuide) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.starttime = trip.getStarttime();
        this.endtime = trip.getEndtime();
        this.startposition = trip.getStartposition();
        this.description = trip.getDescription();
        this.category = trip.getCategory();
        if (includeGuide && trip.getGuide() != null) {
            this.guide = new GuideDTO(trip.getGuide(), false);
        }
    }

    public TripDTO(Trip trip) {
        this(trip, true);
    }

    public static List<TripDTO> toTripDTOList(List<Trip> trips) {
        return trips.stream().map(TripDTO::new).collect(Collectors.toList());
    }
}