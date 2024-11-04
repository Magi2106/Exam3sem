package dat.routes;

import dat.controllers.impl.TripController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoute {
    private final TripController tripController = new TripController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post(tripController::create);
            get(tripController::readAll);
            get("{id}", tripController::read);
            put("{id}", tripController::update);
            delete("{id}", tripController::delete);
            put("{tripId}/guides/{guideId}", tripController::addGuideToTrip);
            get("guides/{guideId}", tripController::getTripsByGuide);
            get("category/{category}", tripController::filterByCategory);
            get("guides/totalprice", tripController::getTotalPriceByGuide);
        };
    }
}