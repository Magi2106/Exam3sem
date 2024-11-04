package dat.dtos;

import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideDTO {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;
    private List<TripDTO> trips;

    public GuideDTO(Guide guide, boolean includeTrips) {
        this.id = guide.getId();
        this.firstname = guide.getFirstname();
        this.lastname = guide.getLastname();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearsOfExperience = guide.getYearsOfExperience();
        if (includeTrips && guide.getTrips() != null) {
            this.trips = guide.getTrips().stream()
                    .map(trip -> new TripDTO(trip, false))
                    .collect(Collectors.toList());
        }
    }

    public GuideDTO(Guide guide) {
        this(guide, true);
    }

    public static List<GuideDTO> toGuideDTOList(List<Guide> guides) {
        return guides.stream().map(GuideDTO::new).collect(Collectors.toList());
    }
}